package cc.coopersoft.construct.corp.services;

import cc.coopersoft.common.cloud.schemas.UserInfo;
import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.corp.model.CorpEmployee;
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
    public String addUser(long corp, UserInfo user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserInfo> entity = new HttpEntity<>(user,headers);


        ResponseEntity<String> restExchange = oAuth2RestTemplate.exchange(
                "http://authenticationservice/admin/trust/add/{org}",
                HttpMethod.PUT, entity ,String.class, corp  );

        return restExchange.getBody();
    }

}
