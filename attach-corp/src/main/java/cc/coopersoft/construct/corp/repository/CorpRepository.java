package cc.coopersoft.construct.corp.repository;

import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.construct.corp.model.Corp;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorpRepository extends CrudRepository<Corp, Long> , JpaSpecificationExecutor<Corp> {


    Optional<Corp> findByInfoGroupIdTypeAndInfoGroupId(GroupIdType type, String number);

    boolean existsByInfoGroupIdTypeAndInfoGroupId(GroupIdType type, String number);

    boolean existsByCorpCodeNotAndInfoGroupIdTypeAndInfoGroupId(long code,GroupIdType type, String number);
}
