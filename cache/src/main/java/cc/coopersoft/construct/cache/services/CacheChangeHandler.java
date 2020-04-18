package cc.coopersoft.construct.cache.services;


import cc.coopersoft.construct.cache.CacheChangeChannel;
import cc.coopersoft.construct.cache.repository.CorpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CacheChangeChannel.class)
@Slf4j
public class CacheChangeHandler {

    private final CorpRepository corpRepository;

    @Autowired
    public CacheChangeHandler(CorpRepository corpRepository) {
        this.corpRepository = corpRepository;
    }

    @StreamListener(CacheChangeChannel.corpChannel)
    public void corpChange(Long code){
        log.debug("remove corp {} cache for change ",code);
        corpRepository.deleteCorp(code);
    }
}
