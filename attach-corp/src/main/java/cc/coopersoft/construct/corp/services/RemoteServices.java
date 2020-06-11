package cc.coopersoft.construct.corp.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface RemoteServices {

    void publishChangeMessage(long code);

    String addUser(long corpCode, boolean manager, UserDetails user);

    void delUser(String id);
}
