package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CorpCacheService extends DataCacheService<Corp.Default,Long>{

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public CorpCacheService(
            CacheDataRepository<Corp.Default,Long> corpRepository,
                        OAuth2RestTemplate oAuth2RestTemplate) {
        super(corpRepository);
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }


    @Override
    protected Corp.Default getData(Long key) {
        ResponseEntity<Corp.Default> restExchange = oAuth2RestTemplate.exchange(
                "http://construct-attach-corp/publish/corp/{code}",
                HttpMethod.GET, null,Corp.Default.class,key);

        return restExchange.getBody();
    }
}
