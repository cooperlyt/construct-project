package cc.coopersoft.construct.project.services;


import cc.coopersoft.common.construct.corp.CorpProperty;
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
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName("test create project");
        projectInfo.setType(ProjectInfo.ProjectType.CIVIL_HOUSE);
        projectInfo.setProperty(ProjectInfo.ProjectProperty.NEW);
        projectInfo.setImportantLevel(ProjectInfo.ImportType.NORMAL);

        ProjectInfoReg infoReg = new ProjectInfoReg();
        infoReg.setInfo(projectInfo);

        JoinCorp joinCorp = new JoinCorp();
        joinCorp.setProperty(CorpProperty.Developer);
        joinCorp.setCode(1l);

        ProjectCorpReg corpReg = new ProjectCorpReg();
        corpReg.getCorps().add(joinCorp);


        ProjectReg projectReg = new ProjectReg();
        projectReg.setCorp(corpReg);
        projectReg.setInfo(infoReg);

        Project project = projectService.patchCreate(projectReg);

        assertEquals(project.getCorp().getCorps().size(), 1);
        assertEquals(project.getDeveloper().getCode(), 1l);
        assertEquals(project.getInfo().getInfo().getType(), ProjectInfo.ProjectType.CIVIL_HOUSE);


        log.debug("===================== test path create complete =====================");

        code = project.getCode();


    }

    @Test
    public void modify(){

        log.debug("===================== begin test path modify =====================");

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName("test modify project");
        projectInfo.setType(ProjectInfo.ProjectType.BOILER);
        projectInfo.setProperty(ProjectInfo.ProjectProperty.NEW);
        projectInfo.setImportantLevel(ProjectInfo.ImportType.NORMAL);

        ProjectInfoReg infoReg = new ProjectInfoReg();
        infoReg.setInfo(projectInfo);


        JoinCorp joinCorp = new JoinCorp();
        joinCorp.setProperty(CorpProperty.Developer);
        joinCorp.setCode(1l);
        joinCorp.setTel("1111");


        ProjectCorpReg corpReg = new ProjectCorpReg();
        corpReg.getCorps().add(joinCorp);


        ProjectReg projectReg = new ProjectReg();
        projectReg.setCorp(corpReg);
        projectReg.setInfo(infoReg);


        Project project = projectService.pathModify(code,projectReg);


        assertEquals(project.getDeveloper().getTel(), "1111");
        assertEquals(project.getCorp().getCorps().size(), 1);
        assertEquals(project.getDeveloper().getCode(), 1l);
        assertEquals(project.getInfo().getInfo().getType(), ProjectInfo.ProjectType.BOILER);

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
        Page<Project> result = projectService.projects(Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty(),Optional.empty());
        assertEquals(result.getTotalElements(), 1l);
    }

    @Test
    public void joinProjects() {
        List<JoinCorp> result = projectService.joinProjects(1l);
        assertEquals(result.size(), 1);
    }


}