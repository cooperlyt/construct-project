package cc.coopersoft.construct.project.repository;

import cc.coopersoft.construct.project.model.JoinCorp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinCorpRepository  extends CrudRepository<JoinCorp, Long> {
}
