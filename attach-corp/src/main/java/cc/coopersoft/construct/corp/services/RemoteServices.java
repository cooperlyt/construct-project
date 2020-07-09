package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.CorpEmployee;

import java.util.List;

public interface RemoteServices {

    void publishChangeMessage(long code);

    void publishUserChangeMessage(List<String> username);

    String addUser(long corp, CorpEmployee user);

}
