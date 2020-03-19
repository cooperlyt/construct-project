package cc.coopersoft.construct.project.repository;

import cc.coopersoft.construct.project.model.ProjectBusiness;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends CrudRepository<ProjectBusiness, Long>, JpaSpecificationExecutor<ProjectBusiness> {
}
