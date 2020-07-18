package cc.coopersoft.trust.gateway.repository.redis;


import cc.coopersoft.trust.gateway.repository.CacheDataRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class UserRedisRepository implements CacheDataRepository<Map<String,String>,String> {

    private static final String HASH_NAME="trust";


    private HashOperations hashOperations;

    private final RedisTemplate<String,Map<String,String>> redisTemplate;

    public UserRedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(String id, Map<String,String> corps) {
        hashOperations.put(HASH_NAME,id,corps);
    }

    @Override
    public void update(String id, Map<String,String> corps) {
        hashOperations.put(HASH_NAME,id,corps);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete(HASH_NAME,id);
    }

    @Override
    public Map<String,String> find(String id) {
        return (Map<String,String>)hashOperations.get(HASH_NAME,id);
    }
}
