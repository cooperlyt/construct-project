package cc.coopersoft.construct.project.repository;

import cc.coopersoft.construct.project.model.BuildReg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildRegRepository extends CrudRepository<BuildReg,Long> {

    Optional<BuildReg> findByProjectCode(long code);
}
