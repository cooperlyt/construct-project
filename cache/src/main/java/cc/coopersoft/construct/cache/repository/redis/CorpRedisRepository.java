package cc.coopersoft.construct.cache.repository.redis;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class CorpRedisRepository implements CacheDataRepository<Corp.Default,Long> {

    private static final String HASH_NAME="corp";


    private HashOperations hashOperations;

    private final RedisTemplate<Long,Corp.Default> redisTemplate;

    public CorpRedisRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(Corp.Default corp) {
        hashOperations.put(HASH_NAME,corp.getCode(),corp);
    }

    @Override
    public void update(Corp.Default corp) {
        hashOperations.put(HASH_NAME,corp.getCode(),corp);
    }

    @Override
    public void delete(Long corp) {
        hashOperations.delete(HASH_NAME,corp);
    }

    @Override
    public Corp.Default find(Long code) {
        return (Corp.Default)hashOperations.get(HASH_NAME,code);
    }
}
