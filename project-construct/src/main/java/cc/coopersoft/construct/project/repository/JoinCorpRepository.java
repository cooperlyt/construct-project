package cc.coopersoft.construct.project.repository;

import cc.coopersoft.construct.project.model.JoinCorp;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinCorpRepository extends CrudRepository<JoinCorp,Long> {


    //Page<JoinCorp> findByRegProjectIsNotNullAndCode(long code, PageRequest pageRequest);

    @EntityGraph(value = "joinCorp.details",type = EntityGraph.EntityGraphType.FETCH)
    List<JoinCorp> findByRegProjectCodeIsNotNullAndCode(long code);
}
