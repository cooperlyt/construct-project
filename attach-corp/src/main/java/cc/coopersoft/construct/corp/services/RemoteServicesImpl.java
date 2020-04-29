package cc.coopersoft.construct.corp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteServicesImpl implements RemoteServices {

    private final Source source;

    @Autowired
    public RemoteServicesImpl(Source source) {
        this.source = source;
    }

    @Override
    public void publishChangeMessage(long code){
        log.debug(" sending message corp {} change ", code);
        source.output().send(MessageBuilder.withPayload(code).build());
    }
}
