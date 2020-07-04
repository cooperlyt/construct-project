package cc.coopersoft.construct.corp.services;


import cc.coopersoft.construct.corp.model.CorpEmployee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class MockRemoteService implements RemoteServices {

    @Override
    public void publishChangeMessage(long code) {
        log.info("notify corp change: " + code);
    }

    @Override
    public String addUser(long corp, CorpEmployee user) {
        return null;
    }

}
