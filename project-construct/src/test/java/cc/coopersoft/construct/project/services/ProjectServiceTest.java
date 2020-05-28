package cc.coopersoft.construct.project.services;


import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.data.OperationType;
import cc.coopersoft.construct.project.Application;
import cc.coopersoft.construct.project.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(classes = {ProjectService.class, MockRemoteService.class})
@Slf4j
@FixMethodOrder(MethodSorters.JVM)
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    private static long code;

    @Test
    public void createAndModify(){

        log.debug("===================== begin test path create =====================");
        ProjectInfoReg projectInfo = new ProjectInfoReg();
        projectInfo.setName("test create project");
        projectInfo.setAddress("test address");
        projectInfo.setType(ProjectInfoReg.Type.CIVIL_HOUSE);
        projectInfo.setProperty(ProjectInfoReg.Property.NEW);
        projectInfo.setImportantType(ProjectInfoReg.ImportantType.NORMAL);



        ProjectCorpReg corpReg = new ProjectCorpReg();

        JoinCorp joinCorp = new JoinCorp();
        joinCorp.setProperty(CorpProperty.Developer);
        joinCorp.setCode(2l);
        corpReg.getCorps().add(joinCorp);

        log.debug("hash code:" + joinCorp.hashCode());

        JoinCorp joinCorp1 = new JoinCorp();
        joinCorp1.setProperty(CorpProperty.Design);
        joinCorp1.setCode(100l);
        corpReg.getCorps().add(joinCorp1);


        log.debug("hash code:" + joinCorp1.hashCode());

        log.debug("equals :" + joinCorp1.equals(joinCorp));

        BuildInfo buildInfo = new BuildInfo();
        buildInfo.setName("test build");
        buildInfo.setStructure(cc.coopersoft.common.construct.project.BuildInfo.Struct.BRICK);

        BuildRegInfo buildRegInfo = new BuildRegInfo();
        buildRegInfo.setInfo(buildInfo);
        buildRegInfo.setOperation(OperationType.CREATE);

        BuildReg buildReg = new BuildReg();
        buildRegInfo.setReg(buildReg);
        buildReg.getBuilds().add(buildRegInfo);

        ProjectReg projectReg = new ProjectReg();
        projectReg.setCorp(corpReg);
        projectReg.setInfo(projectInfo);
        projectReg.setBuild(buildReg);

        Project project = projectService.patchCreate(projectReg);

        assertEquals(project.getCorp().getCorps().size(), 2);
        assertEquals(project.getDeveloper().getCode(), 2l);

        assertEquals(project.getInfo().getType(), ProjectInfoReg.Type.CIVIL_HOUSE);






        log.debug("===================== test path create complete =====================");

        code = project.getCode();


    }

    @Test
    public void modify(){

        log.debug("===================== begin test path modify =====================");

        ProjectInfoReg projectInfo = new ProjectInfoReg();
        projectInfo.setName("test modify project");
        projectInfo.setAddress("test modify address");
        projectInfo.setType(ProjectInfoReg.Type.BOILER);
        projectInfo.setProperty(ProjectInfoReg.Property.NEW);
        projectInfo.setImportantType(ProjectInfoReg.ImportantType.NORMAL);



        JoinCorp joinCorp = new JoinCorp();
        joinCorp.setProperty(CorpProperty.Developer);
        joinCorp.setCode(1l);
        joinCorp.setTel("1111");


        ProjectCorpReg corpReg = new ProjectCorpReg();
        corpReg.getCorps().add(joinCorp);


        ProjectReg projectReg = new ProjectReg();
        projectReg.setCorp(corpReg);
        projectReg.setInfo(projectInfo);


        Project project = projectService.pathModify(code,projectReg);


        assertEquals(project.getDeveloper().getTel(), "1111");
        assertEquals(project.getCorp().getCorps().size(), 1);
        assertEquals(project.getDeveloper().getCode(), 1l);
        assertEquals(project.getInfo().getType(), ProjectInfoReg.Type.BOILER);

        log.debug("===================== test path modify  complete =====================");
    }

    @Test
    public void project() {

        Optional<Project> project = projectService.project(code);
        assertTrue(project.isPresent());
        assertEquals(project.get().getDeveloper().getInfo().getGroupId(), "mock");
    }

    @Test
    public void enableProject() {

        projectService.enableProject(code,false);

        Optional<Project> project = projectService.project(code);
        assertTrue(project.isPresent());
        assertFalse(project.get().isEnable());

        projectService.enableProject(code,true);

        project = projectService.project(code);
        assertTrue(project.isPresent());
        assertTrue(project.get().isEnable());

    }


    @Test
    public void projects() {
        Page<Project> result = projectService.projects(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());
        assertEquals(result.getTotalElements(), 1l);
    }

    @Test
    public void joinProjects() {
        List<JoinCorp> result = projectService.joinProjects(1l);
        assertEquals(result.size(), 1);
    }


}