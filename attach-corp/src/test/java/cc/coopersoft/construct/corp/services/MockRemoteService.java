package cc.coopersoft.construct.corp.services;

import cc.coopersoft.common.cloud.schemas.UserInfo;
import cc.coopersoft.common.construct.corp.*;
import cc.coopersoft.common.data.GroupIdType;
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
    public String addUser(long corp, UserInfo user) {
        return null;
    }

}
