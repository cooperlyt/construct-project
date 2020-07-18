package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.construct.corp.model.CreditRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRecordRepository extends CrudRepository<CreditRecord, Long> {

    List<CreditRecord> findByCorpCodeOrderByRecordTimeDesc(long code);
}
