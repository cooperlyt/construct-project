package cc.coopersoft.construct.cache.repository.redis;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.project.Project;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class ProjectRedisRepository implements CacheDataRepository<Project.Default,Long> {

    private static final String HASH_NAME="project";


    private HashOperations hashOperations;

    private final RedisTemplate<Long, Corp.Default> redisTemplate;

    @Autowired
    public ProjectRedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void update(Project.Default data) {
        hashOperations.put(HASH_NAME,data.getCode(),data);
    }

    @Override
    public void save(Project.Default data) {
        hashOperations.put(HASH_NAME,data.getCode(),data);
    }

    @Override
    public void delete(Long id) {
        hashOperations.delete(HASH_NAME,id);
    }

    @Override
    public Project.Default find(Long id) {
        return (Project.Default) hashOperations.get(HASH_NAME,id);
    }
}
