package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.construct.corp.CorpReg;
import cc.coopersoft.common.construct.corp.RegInfo;
import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.construct.project.model.*;
import cc.coopersoft.construct.project.repository.JoinCorpRepository;
import cc.coopersoft.construct.project.repository.RegRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wujun234.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.*;

@Service
@Slf4j
public class ProjectService {

    private final static  int PAGE_SIZE = 20;

    private final RegRepository regRepository;

    private final ProjectRepository projectRepository;

    private final JoinCorpRepository joinCorpRepository;

    private final RemoteService remoteService;


    @Resource
    private UidGenerator defaultUidGenerator;


    @Autowired
    public ProjectService(RegRepository regRepository,
                          ProjectRepository projectRepository,
                          JoinCorpRepository joinCorpRepository,
                          RemoteService remoteService) {
        this.regRepository = regRepository;
        this.projectRepository = projectRepository;
        this.joinCorpRepository = joinCorpRepository;
        this.remoteService = remoteService;
    }


    public Page<Project> projects(Optional<Boolean> valid,
                                  Optional<ProjectInfo.Property> property,
                                  Optional<ProjectInfo.ProjectClass> projectClass,
                                  Optional<ProjectInfo.ImportantType> important,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){

        boolean validOnly = valid.isEmpty() || valid.get();

        Specification<Project> specification = (Specification<Project>) (root, criteriaQuery, cb) -> {

            boolean countQuery = criteriaQuery.getResultType().equals(Long.class);

            List<Predicate> predicates = new LinkedList<>();


            Join<ProjectInfoReg, ProjectInfo> infoJoin;
            Join<Project,ProjectCorpReg> corpJoin;
            if (countQuery){
                Join<Project,ProjectInfoReg> regJoin = root.join("info", JoinType.LEFT);
                infoJoin = regJoin.join("info", JoinType.LEFT);
                corpJoin = root.join("corp",JoinType.LEFT);
            }else{
                Fetch<Project,ProjectInfoReg> regFetch = root.fetch("info", JoinType.LEFT);
                Fetch<ProjectInfoReg,ProjectInfo> infoFetch = regFetch.fetch("info", JoinType.LEFT);
                infoJoin = (Join<ProjectInfoReg, ProjectInfo>) infoFetch;
                Fetch<Project,ProjectCorpReg> corpFetch = root.fetch("corp",JoinType.LEFT);
                corpJoin = (Join<Project, ProjectCorpReg>) corpFetch;
            }

            if (key.isPresent() && StringUtils.isNotBlank(key.get())){
                List<Predicate> keyPredicate = new LinkedList<>();
                String _key = key.get().trim();
                String _keyLike = "%" + _key + "%";
                keyPredicate.add(cb.equal(root.get("code"),_key));
                keyPredicate.add(cb.like(infoJoin.get("name"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("memo"),_keyLike));
                keyPredicate.add(cb.like(corpJoin.get("tags"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("importantFile"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("address"),_keyLike));

                predicates.add(cb.or(keyPredicate.toArray(new Predicate[0])));
            }

            if (validOnly){
                predicates.add(cb.and(cb.isTrue(root.get("enable").as(Boolean.class))));
            }

            property.ifPresent(value -> predicates.add(cb.and(cb.equal(infoJoin.get("property"), value))));

            projectClass.ifPresent(value -> {
                CriteriaBuilder.In<ProjectInfo.Type> in = cb.in(infoJoin.get("type"));
                for (ProjectInfo.Type type :  value.getSub()){
                    in.value(type);
                }
                predicates.add(cb.and(in));
            });

            important.ifPresent(value -> predicates.add((cb.and(cb.equal(infoJoin.get("importantType"),value)))));


            return cb.and(predicates.toArray(new Predicate[0]));


        };


        Sort sortable = Sort.by((dir.isPresent() ? ("DESC".equals(dir.get()) ? Sort.Direction.DESC : Sort.Direction.ASC) : Sort.Direction.DESC)
                , (sort.isPresent() ? sort.get() : "dataTime"));

        return projectRepository.findAll(specification, PageRequest.of(page.isPresent() ? page.get() : 0 ,PAGE_SIZE,sortable));
    }

    public List<JoinCorp> joinProjects(long code){
        return joinCorpRepository.findByRegProjectCodeIsNotNullAndCode(code);
    }

    public Optional<Project> project(long code){
        return projectRepository.findById(code);
    }

    @Transactional
    public void enableProject(long code, boolean enable){
        remoteService.notifyProjectChange(code);
        Optional<Project> project = project(code);
        if (project.isEmpty()){
            throw new IllegalArgumentException("project is not found! :" + code);
        }
        project.get().setEnable(enable);
        projectRepository.save(project.get());
    }

    @Transactional()
    public Project patchCreate(ProjectReg reg){

        reg = create(reg);
        reg = patch(reg);
        log.debug("save and flush reg.");
        regRepository.saveAndFlush(reg);
        log.debug("reg saved.");

        Project project = new Project();
        project.setCode(reg.getCode());
        project.setEnable(true);
        project.setDataTime(new Date());
        project.setInfo(reg.getInfo());
        project.setCorp(reg.getCorp());
        project.setDeveloper(getDeveloper(reg.getCorp()));

        log.debug("save project.");
        return projectRepository.save(project);
    }

    @Transactional()
    public Project pathModify(long code , ProjectReg reg){
        remoteService.notifyProjectChange(code);
        Optional<Project> project = project(code);
        if (project.isEmpty() || !project.get().isEnable()){
            throw new IllegalArgumentException("project is not found or not enable : " + code);
        }


        reg = modify(project.get(), reg);
        reg = patch(reg);

        regRepository.saveAndFlush(reg);

        if (reg.isInfoMaster()){
            project.get().setInfo(reg.getInfo());
        }
        if (reg.isCorpMaster()){
            project.get().setCorp(reg.getCorp());
            project.get().setDeveloper(getDeveloper(reg.getCorp()));
        }

        project.get().setDataTime(new Date());



        return projectRepository.save(project.get());
    }

    private ProjectReg patch(ProjectReg reg){
        reg.setApplyTime(new Date());
        reg.setRegTime(new Date());
        reg.setSource(RegSource.Patch);
        reg.setStatus(RegStatus.Register);

        if (reg.isCorpMaster()) {
            reg.getCorp().setRegTime(new Date());
        }

        if (reg.isInfoMaster()) {
            reg.getInfo().setRegTime(new Date());
        }
        return reg;
    }

    private JoinCorp getDeveloper(ProjectCorpReg reg){
        for(JoinCorp corp : reg.getCorps()){
            if (CorpProperty.Developer.equals(corp.getProperty())){
                return corp;
            }
        }
        throw new IllegalArgumentException("no Developer found.");
    }



    public boolean projectInReg(long code){
        return regRepository.existsByCodeAndStatus(code, RegStatus.Running);
    }

    private JoinCorpInfo getCorpInfo(JoinCorp joinCorp, long code){

        Corp.Default corp = remoteService.getCorp(code);

        if (corp == null || !corp.isEnable()){
            throw new IllegalArgumentException("corp is not found or not enable: " + code);
        }

        JoinCorpInfo result = new JoinCorpInfo();
        result.setId(joinCorp.getId());
        result.setName(corp.getInfo().getName());
        result.setGroupIdType(corp.getInfo().getGroupIdType());
        result.setGroupId(corp.getInfo().getGroupId());

        for(CorpReg<RegInfo> reg : corp.getRegs()){
            if (joinCorp.getProperty().equals(reg.getProperty())){
                result.setLevel(reg.getInfo().getLevel());
                return result;
            }
        }

        throw new IllegalArgumentException("corp is found ! but not have this property:[" + joinCorp.getProperty() + "]" + code);
    }

    private String addTag(String source, String tag){
        if (StringUtils.isNotBlank(tag)){
            return source.trim() + " " + tag.trim();
        }else{
            return source;
        }
    }

    private ProjectCorpReg tagReg(ProjectCorpReg reg) {
        String tags = "";
        List<CorpSummary> summaries = new ArrayList<>(reg.getCorps().size());
        for(JoinCorp corp: reg.getCorps()){
            tags = tags + " " + corp.getCode();
            tags = addTag(tags,corp.getOutsideTeamFile());
            tags = addTag(tags,corp.getTel());
            tags = addTag(tags,corp.getOutLevelFile());
            tags = addTag(tags,corp.getContacts());
            tags = addTag(tags,corp.getInfo().getGroupId());
            tags = addTag(tags,corp.getInfo().getName());


            summaries.add(new CorpSummary(corp.getProperty(),corp.getCode(),corp.getInfo().getName(),corp.getInfo().getGroupIdType(),corp.getInfo().getGroupId()));
        }
        reg.setTags(tags.trim());
        Collections.sort(summaries);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            reg.setCorpSummary(objectMapper.writeValueAsString(summaries));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("data error can`t convert to json");
        }
        return reg;
    }


    private ProjectReg create(ProjectReg reg){
        reg.setCreateTime(new Date());
        reg.setCode(defaultUidGenerator.getUID());
        reg.setId(reg.getCode());


        reg.setCorpMaster(true);
        reg.getCorp().setId(reg.getId());

        log.debug("corp size:" + reg.getCorp().getCorps().size());
        for(JoinCorp corp: reg.getCorp().getCorps()){
            corp.setId(defaultUidGenerator.getUID());
            corp.setInfo(getCorpInfo(corp,corp.getCode()));
            corp.setReg(reg.getCorp());
        }
        reg.setCorp(tagReg(reg.getCorp()));


        reg.setInfoMaster(true);
        reg.getInfo().setId(reg.getId());
        reg.getInfo().getInfo().setId(reg.getInfo().getId());

        return reg;
    }

    private ProjectReg modify(Project project, ProjectReg reg){
        if (projectInReg(project.getCode())){
            throw new IllegalArgumentException("project in reg business: " + project.getCode());
        }

        reg.setCreateTime(new Date());
        reg.setCode(project.getCode());
        reg.setId(defaultUidGenerator.getUID());
        reg.setCorpMaster(reg.getCorp() != null);

        if (reg.isCorpMaster()){
            reg.getCorp().setId(reg.getId());
            reg.getCorp().setPrevious(project.getCorp());
            for(JoinCorp corp: reg.getCorp().getCorps()){
                corp.setId(defaultUidGenerator.getUID());
                corp.setInfo(getCorpInfo(corp,corp.getCode()));
                corp.setReg(reg.getCorp());
            }
            reg.setCorp(tagReg(reg.getCorp()));
        }else{
            reg.setCorp(project.getCorp());
        }

        reg.setInfoMaster(reg.getInfo() != null);
        if (reg.isInfoMaster()){
            reg.getInfo().setId(reg.getId());
            reg.getInfo().setPrevious(project.getInfo());
            reg.getInfo().getInfo().setId(reg.getInfo().getId());
        }else{
            reg.setInfo(project.getInfo());
        }

        return reg;
    }

}
