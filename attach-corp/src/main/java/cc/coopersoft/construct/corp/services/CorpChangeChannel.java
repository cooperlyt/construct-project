package cc.coopersoft.construct.corp.services;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

public interface CorpChangeChannel {

    String CORP_OUTPUT = "corpChange";

    String USER_OUTPUT = "userChange";

    @Output(CorpChangeChannel.CORP_OUTPUT)
    MessageChannel corpChange();

    @Output(CorpChangeChannel.USER_OUTPUT)
    MessageChannel userChange();
}
