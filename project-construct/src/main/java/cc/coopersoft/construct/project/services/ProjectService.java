package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.construct.corp.CorpReg;
import cc.coopersoft.common.construct.corp.RegInfo;
import cc.coopersoft.common.construct.project.ProjectCorpSummary;
import cc.coopersoft.common.data.BusinessType;
import cc.coopersoft.common.data.OperationType;
import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.construct.project.model.*;
import cc.coopersoft.construct.project.repository.BuildRepository;
import cc.coopersoft.construct.project.repository.JoinCorpRepository;
import cc.coopersoft.construct.project.repository.RegRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
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

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class ProjectService {

    private final static  int PAGE_SIZE = 20;

    private final RegRepository regRepository;

    private final ProjectRepository projectRepository;

    private final JoinCorpRepository joinCorpRepository;

    private final BuildRepository buildRepository;

    private final RemoteService remoteService;


    @Resource
    private UidGenerator cachedUidGenerator;


    @Autowired
    public ProjectService(RegRepository regRepository,
                          ProjectRepository projectRepository,
                          JoinCorpRepository joinCorpRepository,
                          BuildRepository buildRepository, RemoteService remoteService) {
        this.regRepository = regRepository;
        this.projectRepository = projectRepository;
        this.joinCorpRepository = joinCorpRepository;
        this.buildRepository = buildRepository;
        this.remoteService = remoteService;
    }


    public Page<Project> projects(Optional<Boolean> valid,
                                  Optional<ProjectRegInfo.Property> property,
                                  Optional<ProjectRegInfo.ProjectClass> projectClass,
                                  Optional<ProjectRegInfo.ImportantType> important,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){

        boolean validOnly = valid.isEmpty() || valid.get();

        Specification<Project> specification = (Specification<Project>) (root, criteriaQuery, cb) -> {

            boolean countQuery = criteriaQuery.getResultType().equals(Long.class);

            List<Predicate> predicates = new LinkedList<>();


            Join<Project, ProjectRegInfo> infoJoin;
            Join<Project, JoinCorpReg> corpJoin;
            if (countQuery){
                infoJoin = root.join("info", JoinType.LEFT);
                corpJoin = root.join("corp",JoinType.LEFT);
            }else{
                Fetch<Project, ProjectRegInfo> infoFetch = root.fetch("info", JoinType.LEFT);
                infoJoin = (Join<Project, ProjectRegInfo>) infoFetch;
                Fetch<Project, JoinCorpReg> corpFetch = root.fetch("corp",JoinType.LEFT);
                corpJoin = (Join<Project, JoinCorpReg>) corpFetch;
            }

            if (key.isPresent() && StringUtils.isNotBlank(key.get())){
                List<Predicate> keyPredicate = new LinkedList<>();
                String _key = key.get().trim();
                String _keyLike = "%" + _key + "%";
                keyPredicate.add(cb.equal(root.get("code"),_key));
                keyPredicate.add(cb.like(infoJoin.get("name"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("memo"),_keyLike));
                keyPredicate.add(cb.like(corpJoin.get("corpTags"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("importantFile"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("address"),_keyLike));

                predicates.add(cb.or(keyPredicate.toArray(new Predicate[0])));
            }

            if (validOnly){
                predicates.add(cb.and(cb.isTrue(root.get("enable").as(Boolean.class))));
            }

            property.ifPresent(value -> predicates.add(cb.and(cb.equal(infoJoin.get("property"),value))));


            projectClass.ifPresent(value -> {
                CriteriaBuilder.In<ProjectRegInfo.Type> in = cb.in(infoJoin.get("type"));
                for (ProjectRegInfo.Type type :  value.getSub()){
                    in.value(type);
                }
                predicates.add(cb.and(in));
            });

            important.ifPresent(value -> predicates.add((cb.and(cb.equal(infoJoin.get("importantType"),value)))));


            return cb.and(predicates.toArray(new Predicate[0]));


        };


        Sort sortable = Sort.by((dir.isPresent() ? ("DESC".equals(dir.get()) ? Sort.Direction.DESC : Sort.Direction.ASC) : Sort.Direction.DESC)
                , (sort.isPresent() ? sort.get() : "regTime"));

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

    private void saveBuildRecord(BuildReg buildReg, long projectCode){
        for(BuildRegInfo build: buildReg.getBuilds()){
            switch (build.getOperation()){

                case DELETE:
                    buildRepository.deleteById(build.getCode());
                    break;
                case MODIFY:
                    Build buildRecord = buildRepository.findById(build.getCode()).orElseThrow();
                    if (buildRecord.getInfo().getId().equals(build.getPrevious().getId())){
                        buildRecord.setInfo(build.getInfo());
                        buildRecord.setProjectCode(projectCode);
                        buildRecord.setRegTime(buildReg.getRegTime());
                        buildRepository.saveAndFlush(buildRecord);
                    }

                    break;
                case CREATE:
                    Build newBuildRecord = new Build();
                    newBuildRecord.setCode(build.getCode());
                    newBuildRecord.setInfo(build.getInfo());
                    newBuildRecord.setRegTime(buildReg.getRegTime());
                    newBuildRecord.setEnable(true);
                    newBuildRecord.setProjectCode(projectCode);
                    buildRepository.saveAndFlush(newBuildRecord);
                    break;
            }
        }
    }

    @Transactional()
    public Project patchCreate(ProjectReg reg){

        reg = create(reg);
        reg = patch(reg);
        log.debug("save and flush reg.");
        regRepository.saveAndFlush(reg);
        log.debug("reg saved.");

        saveBuildRecord(reg.getBuild(),reg.getCode());

        Project project = new Project();
        project.setCode(reg.getCode());
        project.setEnable(true);
        project.setRegTime(new Date());
        project.setInfo(reg.getInfo());
        project.setCorp(reg.getCorp());
        project.setBuild(reg.getBuild());
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

        if (reg.isBuildMaster()){
            saveBuildRecord(reg.getBuild(),reg.getCode());
            project.get().setBuild(reg.getBuild());
        }

        if (reg.isInfoMaster()){
            project.get().setInfo(reg.getInfo());
        }
        if (reg.isCorpMaster()){
            project.get().setCorp(reg.getCorp());
            project.get().setDeveloper(getDeveloper(reg.getCorp()));
        }

        project.get().setRegTime(new Date());



        return projectRepository.save(project.get());
    }

    private ProjectReg patch(ProjectReg reg){
        reg.setApplyTime(new Date());
        reg.setRegTime(new Date());
        reg.setSource(RegSource.Patch);
        reg.setStatus(RegStatus.Register);

        if(reg.isBuildMaster()){
            reg.getBuild().setRegTime(new Date());
        }

        if (reg.isCorpMaster()) {
            reg.getCorp().setRegTime(new Date());
        }

        if (reg.isInfoMaster()) {
            reg.getInfo().setRegTime(new Date());
        }
        return reg;
    }

    private JoinCorp getDeveloper(JoinCorpReg reg){
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
        //result.setId(joinCorp.getId());
        result.setCorp(joinCorp);
        result.setName(corp.getInfo().getName());
        result.setGroupIdType(corp.getInfo().getGroupIdType());
        result.setGroupId(corp.getInfo().getGroupId());
        result.setOwnerId(corp.getInfo().getOwnerId());
        result.setOwnerIdType(corp.getInfo().getOwnerIdType());
        result.setOwnerName(corp.getInfo().getOwnerName());

        for(CorpReg<RegInfo> reg : corp.getRegs()){
            if (joinCorp.getProperty().equals(reg.getProperty())){
                result.setLevel(reg.getInfo().getLevel());
                return result;
            }
        }

        throw new IllegalArgumentException("corp is found ! but not have this property:[" + joinCorp.getProperty() + "]" + code);
    }

    private void initBuildReg(BuildReg buildReg){
        int count = 0;
        BigDecimal onArea = BigDecimal.ZERO;
        BigDecimal underArea = BigDecimal.ZERO;
        for(BuildRegInfo buildRegInfo: buildReg.getBuilds()){
            if (!OperationType.DELETE.equals(buildRegInfo.getOperation())){
                count++;
                if (buildRegInfo.getInfo().getOnArea() != null)
                    onArea = onArea.add(buildRegInfo.getInfo().getOnArea());
                if (buildRegInfo.getInfo().getUnderArea() != null){
                    underArea = underArea.add(buildRegInfo.getInfo().getUnderArea());
                }
            }
            buildRegInfo.setId(cachedUidGenerator.getUID());
            if (OperationType.MODIFY.equals(buildRegInfo.getOperation())){
                buildRegInfo.getInfo().setId(buildRegInfo.getId());
                buildRegInfo.setPrevious(buildRepository.findById(buildRegInfo.getCode()).orElseThrow().getInfo());
            }else if (OperationType.CREATE.equals(buildRegInfo.getOperation())){
                buildRegInfo.getInfo().setId(buildRegInfo.getId());
                buildRegInfo.setCode(buildRegInfo.getId());
            }
        }
        buildReg.setCount(count);
        buildReg.setOnArea(onArea);
        buildReg.setUnderArea(underArea);
    }

    private ProjectReg create(ProjectReg reg){
        reg.setType(BusinessType.CREATE);
        reg.setCreateTime(new Date());
        reg.setCode(cachedUidGenerator.getUID());
        reg.setId(reg.getCode());


        reg.setCorpMaster(true);
        reg.getCorp().setId(reg.getId());


        reg.setBuildMaster(true);
        reg.getBuild().setId(reg.getId());
        initBuildReg(reg.getBuild());

        log.debug("corp size:" + reg.getCorp().getCorps().size());
        for(JoinCorp corp: reg.getCorp().getCorps()){
            corp.setId(cachedUidGenerator.getUID());
            corp.setInfo(getCorpInfo(corp,corp.getCode()));
            corp.setReg(reg.getCorp());
        }
       // test(reg.getCorp().getCorps());
        ProjectCorpSummary.fillProjectCorpSummary(reg.getCorp(),reg.getCorp().getCorps());

        reg.setInfoMaster(true);
        reg.getInfo().setId(reg.getId());

        return reg;
    }

    private ProjectReg modify(Project project, ProjectReg reg){
        if (projectInReg(project.getCode())){
            throw new IllegalArgumentException("project in reg business: " + project.getCode());
        }

        reg.setType(BusinessType.MODIFY);
        reg.setCreateTime(new Date());
        reg.setCode(project.getCode());
        reg.setId(cachedUidGenerator.getUID());
        reg.setCorpMaster(reg.getCorp() != null);

        if (reg.isCorpMaster()){
            reg.getCorp().setId(reg.getId());
            reg.getCorp().setPrevious(project.getCorp());
            for(JoinCorp corp: reg.getCorp().getCorps()){
                corp.setId(cachedUidGenerator.getUID());
                corp.setInfo(getCorpInfo(corp,corp.getCode()));
                corp.setReg(reg.getCorp());
            }
            ProjectCorpSummary.fillProjectCorpSummary(reg.getCorp(),reg.getCorp().getCorps());
        }else{
            reg.setCorp(project.getCorp());
        }

        reg.setInfoMaster(reg.getInfo() != null);
        if (reg.isInfoMaster()){
            reg.getInfo().setId(reg.getId());
            reg.getInfo().setPrevious(project.getInfo());
        }else{
            reg.setInfo(project.getInfo());
        }

        reg.setBuildMaster(reg.getBuild() != null);
        if (reg.isBuildMaster()){
            reg.getBuild().setId(reg.getId());
            initBuildReg(reg.getBuild());
        }else{
            reg.setBuild(project.getBuild());
        }

        return reg;
    }

}
