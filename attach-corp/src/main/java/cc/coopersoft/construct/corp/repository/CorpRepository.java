package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.construct.corp.model.Corp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorpRepository extends CrudRepository<Corp, Long> , JpaSpecificationExecutor<Corp> {


    Optional<Corp> findByInfoGroupIdTypeAndInfoGroupId(GroupIdType type, String number);

    boolean existsByInfoGroupIdTypeAndInfoGroupId(GroupIdType type, String number);

    boolean existsByCodeNotAndInfoGroupIdTypeAndInfoGroupId(long code, GroupIdType type, String number);

    @EntityGraph(value = "corp.full",type = EntityGraph.EntityGraphType.FETCH)
    Page<Corp> findByEnableIsTrueAndInfoNameLikeOrEnableIsTrueAndInfoGroupIdLike(String name, String groupId, Pageable pageable);

    @EntityGraph(value = "corp.full",type = EntityGraph.EntityGraphType.FETCH)
    Page<Corp> findByEnableIsTrue(Pageable pageable);



}
