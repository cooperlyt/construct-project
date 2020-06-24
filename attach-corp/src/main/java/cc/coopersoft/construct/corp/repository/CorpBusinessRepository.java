package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpBusiness;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CorpBusinessRepository extends CrudRepository<CorpBusiness, Long>, JpaSpecificationExecutor<Corp>, JpaRepository<CorpBusiness,Long> {

    boolean existsByStatusAndCode(RegStatus status, Long corpCode);

    @EntityGraph(value = "business.full", type = EntityGraph.EntityGraphType.FETCH)
    List<CorpBusiness> findByStatusInAndCodeOrderByCreateTime(Set<RegStatus> statuses, Long corpCode);

    boolean existsByStatusAndInfoGroupIdTypeAndInfoGroupId(RegStatus status, GroupIdType type, String number);

    boolean existsByCodeNotAndStatusAndInfoGroupIdTypeAndInfoGroupId(long code, RegStatus status, GroupIdType type, String number);

}
