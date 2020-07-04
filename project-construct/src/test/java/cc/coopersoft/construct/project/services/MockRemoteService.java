package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.*;
import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.common.data.PersonIdType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Primary
@Slf4j
public class MockRemoteService implements RemoteService {
    @Override
    public Mono<Corp.Default> getCorp(long code) {

        log.info("get mock corp : " + code);
        CorpInfo info = new CorpInfo();
        info.setName("mock corp");
        info.setGroupId("mock");
        info.setGroupIdType(GroupIdType.COMPANY_CODE);
        info.setOwnerIdType(PersonIdType.TW_ID);
        info.setOwnerName("test owner");



        RegInfo regInfo = new RegInfo();
        regInfo.setLevel(1);

        CorpReg<RegInfo> reg = new CorpReg<>();
        reg.setInfo(regInfo);

        if (code == 100l){
            reg.setProperty(CorpProperty.Design);
        }else
            reg.setProperty(CorpProperty.Developer);

        Corp.Default result = new Corp.Default();
        result.setInfo(info);
        result.getRegs().add(reg);
        result.setCode(code);
        result.setEnable(true);
        return Mono.just(result);
    }

    @Override
    public void notifyProjectChange(long code) {

        log.info("notify project change: " + code);
    }
}
