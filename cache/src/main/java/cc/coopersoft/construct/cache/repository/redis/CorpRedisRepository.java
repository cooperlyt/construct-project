package cc.coopersoft.construct.cache.repository.redis;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.cache.repository.CorpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Repository
public class CorpRedisRepository implements CorpRepository {

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

    @Override
    public void saveCorp(Corp.Default corp) {
        hashOperations.put(HASH_NAME,corp.getCode(),corp);
    }

    @Override
    public void updateCorp(Corp.Default corp) {
        hashOperations.put(HASH_NAME,corp.getCode(),corp);
    }

    @Override
    public void deleteCorp(long corp) {
        hashOperations.delete(HASH_NAME,corp);
    }

    @Override
    public Corp.Default findCorp(long code) {
        return (Corp.Default)hashOperations.get(HASH_NAME,code);
    }
}
