package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
@Slf4j
public class CorpCacheService extends DataCacheService<Corp.Default,Long>{

    private final WebClient webClient;


    @Autowired
    public CorpCacheService(
            CacheDataRepository<Corp.Default, Long> corpRepository, WebClient webClient) {
        super(corpRepository);
        this.webClient = webClient;
    }


    @Override
    protected Mono<Corp.Default> getData(Long key) {
        return webClient
                .get()
                .uri("http://construct-attach-corp/publish/corp/{code}",key)
                .attributes(clientRegistrationId("master-extend-cer"))
                .retrieve()
                .bodyToMono(Corp.Default.class);

    }
}
