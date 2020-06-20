package cc.coopersoft.construct.project.services;


import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.construct.project.model.JoinCorp;
import cc.coopersoft.construct.project.model.JoinCorpReg;
import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectRegInfo;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class TrustService {

    private final ProjectRepository projectRepository;

    public TrustService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> searchProject(long corp, String key, CorpProperty property){


        Specification<Project> specification = (Specification<Project>) (root, criteriaQuery, cb) -> {

            criteriaQuery.distinct(true);
            //boolean countQuery = criteriaQuery.getResultType().equals(Long.class);

            List<Predicate> predicates = new LinkedList<>();
            predicates.add(cb.and(cb.isTrue(root.get("enable").as(Boolean.class))));

            Join<Project, JoinCorpReg> corpRegJoin = root.join("corp",JoinType.LEFT);


            Join<JoinCorpReg,JoinCorp> corpJoin = corpRegJoin.join("corps",JoinType.LEFT);

            predicates.add(cb.and(cb.equal(corpJoin.get("code"),corp)));

            if (property != null){
                predicates.add(cb.and(cb.equal(corpJoin.get("property"),property)));
            }

            Fetch<Project, ProjectRegInfo> infoFetch = root.fetch("info", JoinType.LEFT);
            Join<Project, ProjectRegInfo> infoJoin =  (Join<Project, ProjectRegInfo>) infoFetch;;



            if (key != null && StringUtils.isNotBlank(key)){
                List<Predicate> keyPredicate = new LinkedList<>();
                String _key = key.trim();
                String _keyLike = "%" + _key + "%";
                keyPredicate.add(cb.equal(root.get("code").as(String.class),_key));
                keyPredicate.add(cb.like(infoJoin.get("name"),_keyLike));
                keyPredicate.add(cb.like(corpJoin.get("corpTags"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("importantFile"),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("address"),_keyLike));

                predicates.add(cb.or(keyPredicate.toArray(new Predicate[0])));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return projectRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "regTime"));
    }
}
