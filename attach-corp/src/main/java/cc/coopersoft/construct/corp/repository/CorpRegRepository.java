package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.construct.corp.model.CorpReg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorpRegRepository extends CrudRepository<CorpReg, Long> {

    Optional<CorpReg> findByIdCorpCodeAndIdProperty(long corpCode, CorpProperty type);

}
