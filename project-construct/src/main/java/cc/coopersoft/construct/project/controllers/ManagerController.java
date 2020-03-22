package cc.coopersoft.construct.project.controllers;

import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectInfo;
import cc.coopersoft.construct.project.model.ProjectReg;
import cc.coopersoft.construct.project.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="mgr")
public class ManagerController {

    private final ProjectService projectService;

    @Autowired
    public ManagerController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/enable", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(){
        this.projectService.enableProject(true);
    }

    @RequestMapping(value = "/disable", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(){
        this.projectService.enableProject(false);
    }

    @RequestMapping(value = "/patch/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Project patchCreate(ProjectReg reg){
        return projectService.patchCreate(reg);
    }

    @RequestMapping(value = "/patch/modify", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Project pathModify(ProjectReg reg){
        return projectService.pathModify(reg);
    }
}
