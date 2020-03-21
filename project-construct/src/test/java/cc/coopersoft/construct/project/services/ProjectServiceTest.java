package cc.coopersoft.construct.project.services;


import cc.coopersoft.construct.project.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(classes = {ProjectService.class})
public class ProjectServiceTest {

    @Test
    public void createAndModify(){
        //TODO test
        assertTrue(true);
    }

    @Test
    public void projects() {
        //TODO test
        assertTrue(true);

    }

    @Test
    public void businesses() {
        //TODO test
        assertTrue(true);
    }

    @Test
    public void project() {
        //TODO test
        assertTrue(true);
    }

    @Test
    public void business() {
        //TODO test
        assertTrue(true);
    }

    @Test
    public void joinProjects() {
        //TODO test
        assertTrue(true);
    }

    @Test
    public void enableProject() {
        //TODO test
        assertTrue(true);
    }
}