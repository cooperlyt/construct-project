package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.Corp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;


@Service
@Slf4j
public class RemoteServiceImpl implements RemoteService{

    private final Source source;

    private final WebClient webClient;

    public RemoteServiceImpl(Source source, WebClient webClient) {
        this.source = source;
        this.webClient = webClient;
    }

    @Override
    public Mono<Corp.Default> getCorp(long code){

        return webClient
                .get()
                .uri("http://construct-project-cache/data/corp/{code}",code)
                .attributes(clientRegistrationId("master-extend-cer"))
                .retrieve()
                .bodyToMono(Corp.Default.class);
    }

    @Override
    public void notifyProjectChange(long code){
        log.debug(" sending message project {} change ", code);
        source.output().send(MessageBuilder.withPayload(code).build());
    }
}
