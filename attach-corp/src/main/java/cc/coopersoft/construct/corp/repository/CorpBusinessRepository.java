package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.common.business.BusinessStatus;
import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpBusiness;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CorpBusinessRepository extends CrudRepository<CorpBusiness, Long>, JpaSpecificationExecutor<Corp> {

    boolean existsByStatusAndCorpCode(BusinessStatus status, Long corpCode);

    @EntityGraph(value = "business.full", type = EntityGraph.EntityGraphType.FETCH)
    List<CorpBusiness> findByStatusInAndCorpCodeOrderByCreateTime(Set<BusinessStatus> statuses, Long corpCode);
}
