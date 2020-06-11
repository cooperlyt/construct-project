package cc.coopersoft.construct.project.services;

import cc.coopersoft.construct.project.model.*;
import cc.coopersoft.construct.project.repository.BuildRegRepository;
import cc.coopersoft.construct.project.repository.JoinCorpRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectServices {

    private final static  int PAGE_SIZE = 20;

    private final JoinCorpRepository joinCorpRepository;

    private final ProjectRepository projectRepository;

    private final BuildRegRepository buildRegRepository;

    private final RemoteService remoteService;

    public ProjectServices(JoinCorpRepository joinCorpRepository, ProjectRepository projectRepository, BuildRegRepository buildRegRepository, RemoteService remoteService) {
        this.joinCorpRepository = joinCorpRepository;
        this.projectRepository = projectRepository;
        this.buildRegRepository = buildRegRepository;
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


        Sort sortable = Sort.by((dir.filter(s -> !"DESC".equals(s)).map(s -> Sort.Direction.ASC).orElse(Sort.Direction.DESC))
                , (sort.orElse("regTime")));

        return projectRepository.findAll(specification, PageRequest.of(page.orElse(0),PAGE_SIZE,sortable));
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


    public Optional<BuildReg> getBuildRegByProject(long code){
        return buildRegRepository.findByProjectCode(code);
    }
}
