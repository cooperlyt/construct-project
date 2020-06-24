package cc.coopersoft.construct.corp.services;

import cc.coopersoft.common.cloud.schemas.UserInfo;
import cc.coopersoft.construct.corp.model.CorpEmployee;
import org.springframework.security.core.userdetails.UserDetails;

public interface RemoteServices {

    void publishChangeMessage(long code);

    String addUser(long corp, UserInfo user);

}
