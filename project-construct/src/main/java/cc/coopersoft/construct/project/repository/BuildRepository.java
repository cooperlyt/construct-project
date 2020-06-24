package cc.coopersoft.construct.project.repository;

import cc.coopersoft.construct.project.model.Build;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildRepository extends CrudRepository<Build,Long>, JpaRepository<Build,Long> {
}
