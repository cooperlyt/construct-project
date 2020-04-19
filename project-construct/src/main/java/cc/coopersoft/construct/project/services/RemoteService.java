package cc.coopersoft.construct.project.services;


import cc.coopersoft.common.construct.corp.Corp;

public interface RemoteService {

    Corp.Default getCorp(long code);

    void notifyProjectChange(long code);
}
