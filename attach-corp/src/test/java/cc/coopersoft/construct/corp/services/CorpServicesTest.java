package cc.coopersoft.construct.corp.services;

import cc.coopersoft.common.data.ConstructJoinType;
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
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(classes = {CorpServices.class})
public class CorpServicesTest {

    @Autowired
    private CorpServices corpServices;


    private static final Logger logger = LoggerFactory.getLogger(CorpServicesTest.class);



    @Test
    public void patchCreate() {



        CorpBusiness business = new CorpBusiness();

        CorpInfo corpInfo = new CorpInfo();
        corpInfo.setGroupIdType(GroupIdType.COMPANY_CODE);
        corpInfo.setGroupId("path_create_test");
        corpInfo.setName("test1");
        corpInfo.setOwnerIdType(PersonIdType.MASTER_ID);
        corpInfo.setOwnerId("test");
        corpInfo.setOwnerName("test");

        business.setCorpInfo(corpInfo);

        RegInfo regInfo = new RegInfo();
        regInfo.setRegTo(new Date());
        regInfo.setLevel(0);
        regInfo.setLevelNumber("test1");
        regInfo.setType(ConstructJoinType.Construct);

        BusinessReg businessReg = new BusinessReg();

        businessReg.setInfo(regInfo);
        business.getRegs().add(businessReg);

        regInfo = new RegInfo();
        regInfo.setRegTo(new Date());
        regInfo.setLevel(0);
        regInfo.setLevelNumber("test2");
        regInfo.setType(ConstructJoinType.Developer);
        businessReg = new BusinessReg();
        businessReg.setInfo(regInfo);
        business.getRegs().add(businessReg);

        corpServices.patchCreate(business);


        assertTrue(corpServices.corp("path_create_test").get().getTypes().equals("Construct Developer") ||
                corpServices.corp("path_create_test").get().getTypes().equals("Developer Construct"));

    }

    @Test
    public  void patchModify() {



    }
}