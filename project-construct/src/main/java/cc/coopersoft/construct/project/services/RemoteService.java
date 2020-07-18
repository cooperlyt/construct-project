package cc.coopersoft.construct.project.services;


import cc.coopersoft.common.construct.corp.Corp;
import reactor.core.publisher.Mono;

public interface RemoteService {

    Mono<Corp.Default> getCorp(long code);

    void notifyProjectChange(long code);
}
