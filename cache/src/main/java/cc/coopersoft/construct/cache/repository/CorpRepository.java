package cc.coopersoft.construct.cache.repository;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.corp.CorpInfo;
import cc.coopersoft.common.construct.corp.CorpReg;
import cc.coopersoft.common.construct.corp.RegInfo;

public interface CorpRepository {

    void saveCorp(Corp.Default corp);
    void updateCorp(Corp.Default corp);
    void deleteCorp(long code);
    Corp.Default findCorp(long code);
}
