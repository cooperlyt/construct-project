package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.construct.corp.model.CorpEmployee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorpEmployeeRepository extends CrudRepository<CorpEmployee,String> {

    List<CorpEmployee> findByCorpCodeOrderByCode(long code);
}
