package cc.coopersoft.construct.corp.services;

import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.common.data.PersonIdType;
import cc.coopersoft.construct.corp.Application;
import cc.coopersoft.construct.corp.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(classes = {CorpServices.class})
public class CorpServicesTest {

    @Autowired
    private CorpServices corpServices;


    private static final Logger logger = LoggerFactory.getLogger(CorpServicesTest.class);



    @Test
    public void curd() {

        // create

        CorpBusiness business = new CorpBusiness();

        CorpInfo corpInfo = new CorpInfo();
        corpInfo.setGroupIdType(GroupIdType.COMPANY_CODE);
        corpInfo.setGroupId("path_create_test");
        corpInfo.setName("test1");
        corpInfo.setOwnerIdType(PersonIdType.MASTER_ID);
        corpInfo.setOwnerId("test");
        corpInfo.setOwnerName("test");

        business.setInfo(corpInfo);

        RegInfo regInfo = new RegInfo();
        regInfo.setRegTo(new Date());
        regInfo.setLevel(0);
        regInfo.setLevelNumber("test1");

        BusinessReg businessReg = new BusinessReg();
        businessReg.setInfo(regInfo);
        businessReg.setId(new BusinessRegPK());
        businessReg.getId().setProperty(CorpProperty.Construct);
        business.getRegs().add(businessReg);

        regInfo = new RegInfo();
        regInfo.setRegTo(new Date());
        regInfo.setLevel(0);
        regInfo.setLevelNumber("test2");
        businessReg = new BusinessReg();
        businessReg.setInfo(regInfo);
        businessReg.setId(new BusinessRegPK());
        businessReg.getId().setProperty(CorpProperty.Developer);
        business.getRegs().add(businessReg);

        long code = corpServices.patchCreate(business).getCode();


        assertTrue(corpServices.corp(code).get().getTypes().equals("Construct Developer") ||
                corpServices.corp(code).get().getTypes().equals("Developer Construct"));

        assertEquals(corpServices.corp(code).get().getRegs().size(), 2);


        logger.debug("------------create test completed!  ----------------------");

        business = new CorpBusiness();
        businessReg = new BusinessReg();
        businessReg.setId(new BusinessRegPK());
        businessReg.getId().setProperty(CorpProperty.Construct);
        businessReg.setOperateType(BusinessReg.OperateType.DELETE);
        business.getRegs().add(businessReg);

        corpServices.patchModify(code,business);

        assertEquals(corpServices.corp(code).get().getTypes(),"Developer");

        assertEquals(corpServices.corp(code).get().getRegs().size(), 1);


        logger.debug("------------del reg  test completed!  ----------------------");

        business = new CorpBusiness();

        regInfo = new RegInfo();
        regInfo.setRegTo(new Date());
        regInfo.setLevel(0);
        regInfo.setLevelNumber("test2");

        businessReg = new BusinessReg();
        businessReg.setId(new BusinessRegPK());
        businessReg.getId().setProperty(CorpProperty.Construct);
        businessReg.setInfo(regInfo);
        businessReg.setOperateType(BusinessReg.OperateType.CREATE);
        business.getRegs().add(businessReg);


        regInfo = new RegInfo();
        regInfo.setRegTo(new Date());
        regInfo.setLevel(2);
        regInfo.setLevelNumber("test1-mod");

        businessReg = new BusinessReg();
        businessReg.setId(new BusinessRegPK());
        businessReg.getId().setProperty(CorpProperty.Developer);
        businessReg.setInfo(regInfo);
        businessReg.setOperateType(BusinessReg.OperateType.MODIFY);
        business.getRegs().add(businessReg);

        corpServices.patchModify(code,business);

        assertTrue(corpServices.corp(code).get().getTypes().equals("Construct Developer") ||
                corpServices.corp(code).get().getTypes().equals("Developer Construct"));

        assertEquals(corpServices.corp(code).get().getRegs().size(), 2);


        assertEquals(corpServices.corpReg(code,CorpProperty.Developer ).get().getInfo().getLevelNumber(),"test1-mod");

        logger.debug("------------modify create and modify  test completed!  ----------------------");


        logger.debug(" test info change");


        corpInfo = new CorpInfo();
        corpInfo.setGroupIdType(GroupIdType.COMPANY_CODE);
        corpInfo.setGroupId("path_create_test");
        corpInfo.setName("test1");
        corpInfo.setOwnerIdType(PersonIdType.MASTER_ID);
        corpInfo.setOwnerId("test-modify");
        corpInfo.setOwnerName("test");

        business = new CorpBusiness();

        business.setInfo(corpInfo);

        Corp corp = corpServices.patchModify(code,business);

        assertEquals(corp.getInfo().getOwnerId(), "test-modify");


    }


}