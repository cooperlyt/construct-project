package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.cache.repository.CorpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {

    private final OAuth2RestTemplate oAuth2RestTemplate;

    private final CorpRepository corpRepository;

    @Autowired
    public CacheService(CorpRepository corpRepository,OAuth2RestTemplate oAuth2RestTemplate) {
        this.corpRepository = corpRepository;
        this.oAuth2RestTemplate = oAuth2RestTemplate;

    }


    private Corp.Default checkCorpCache(long code){
        try{
            return corpRepository.findCorp(code);
        }catch (Exception ex){
            log.error("Error encountered while trying to retrieve corp {} check Cache.Exception {}", code, ex);
            return null;
        }
    }

    private void cacheCorp(Corp.Default corp){
        try{
            corpRepository.saveCorp(corp);
        }catch (Exception ex){
            log.error("Unable to  cache corp {}. exception {}", corp.getCode(), ex);
        }
    }


    public Corp.Default getCorp(long code){
        Corp.Default corp = checkCorpCache(code);
        if (corp != null){
            log.debug(" successfully retrieved an corp {} from the cache: {}", code, corp);
            return corp;
        }

        log.debug("Unable to locate corp from the cache: {}",code);

        ResponseEntity<Corp.Default> restExchange = oAuth2RestTemplate.exchange(
                "http://construct-attach-corp/view/corp/{code}",
                HttpMethod.GET, null,Corp.Default.class,code);

        corp = restExchange.getBody();

        if (corp != null){
            cacheCorp(corp);
        }

        return corp;
    }
}
