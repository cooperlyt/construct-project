package cc.coopersoft.construct.corp.services;

import cc.coopersoft.common.construct.corp.Corp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteServicesImpl implements RemoteServices {

    private static final String CORP_TYPE = "CONSTRUCT.CORP";

    private final Source source;

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public RemoteServicesImpl(Source source, OAuth2RestTemplate oAuth2RestTemplate) {
        this.source = source;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @Override
    public void publishChangeMessage(long code){
        log.debug(" sending message corp {} change ", code);
        source.output().send(MessageBuilder.withPayload(code).build());
    }

    @Override
    public String addUser(long corpCode, boolean manager, UserDetails user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDetails> entity = new HttpEntity<>(user,headers);

        ResponseEntity<String> restExchange = oAuth2RestTemplate.exchange(
                "http://authenticationservice/admin/trust/add/{type}/{org}/{manager}",
                HttpMethod.POST, entity ,String.class, CORP_TYPE,corpCode,manager );

        return restExchange.getBody();
    }

    @Override
    public void delUser(String id) {
        ResponseEntity<String> restExchange = oAuth2RestTemplate.exchange(
                "http://authenticationservice/admin/trust/del/{type}/{org}/{username}",
                HttpMethod.DELETE, null ,String.class, CORP_TYPE,corpCode,manager );

    }
}
