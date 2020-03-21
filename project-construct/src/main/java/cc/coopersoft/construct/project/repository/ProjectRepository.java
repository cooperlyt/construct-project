package cc.coopersoft.construct.project.repository;

import cc.coopersoft.construct.project.model.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
