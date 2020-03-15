package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.common.business.BusinessStatus;
import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpBusiness;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CorpBusinessRepository extends CrudRepository<CorpBusiness, Long>, JpaSpecificationExecutor<Corp> {

    boolean existsByStatusAndCorpInfoCorpCode(BusinessStatus status, String corpCode);

    List<CorpBusiness> findByStatusInAndCorpInfoCorpCodeOrderByCreateTime(Set<BusinessStatus> statuses, String corpCode);
}
