package cc.coopersoft.construct.cache;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CacheChangeChannel {

    static String corpChannel = "corpChanges";

    static String projectChannel = "projectChanges";

    @Input(corpChannel)
    SubscribableChannel corp();

    @Input(projectChannel)
    SubscribableChannel project();
}
