package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.CorpEmployee;

public interface RemoteServices {

    void publishChangeMessage(long code);

    String addUser(long corp, CorpEmployee user);

}
