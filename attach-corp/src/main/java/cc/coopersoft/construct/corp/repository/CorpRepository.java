package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.construct.corp.model.Corp;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorpRepository extends CrudRepository<Corp, String> , JpaSpecificationExecutor<Corp> {

}
