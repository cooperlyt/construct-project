package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.Corp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteServiceImpl implements RemoteService{

    private final OAuth2RestTemplate oAuth2RestTemplate;
    private final Source source;

    public RemoteServiceImpl(OAuth2RestTemplate oAuth2RestTemplate, Source source) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.source = source;
    }

    @Override
    public Corp.Default getCorp(long code){
        ResponseEntity<Corp.Default> restExchange = oAuth2RestTemplate.exchange(
                "http://construct-project-cache/data/corp/{code}",
                HttpMethod.GET, null,Corp.Default.class,code);
        return restExchange.getBody();
    }

    @Override
    public void notifyProjectChange(long code){
        log.debug(" sending message corp {} change ", code);
        source.output().send(MessageBuilder.withPayload(code).build());
    }
}
