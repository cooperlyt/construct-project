package cc.coopersoft.construct.project.controllers;

import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectInfo;
import cc.coopersoft.construct.project.model.ProjectReg;
import cc.coopersoft.construct.project.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="mgr")
public class ManagerController {

    private final ProjectService projectService;

    @Autowired
    public ManagerController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/enable/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String enable(@PathVariable("code") long code){
        this.projectService.enableProject(code,true);
        return "{ \"code\":" +  code + " , \"enable\":true}" ;
    }

    @RequestMapping(value = "/disable/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String disable(@PathVariable("code") long code){
        this.projectService.enableProject(code,false);
        return "{ \"code\":" +  code + " , \"enable\":false}" ;
    }

    @RequestMapping(value = "/patch/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String patchCreate(@RequestBody @Valid ProjectReg reg){
        return String.valueOf(projectService.patchCreate(reg).getCode());
    }

    @RequestMapping(value = "/patch/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String pathModify(@PathVariable("code") long code, @RequestBody @Valid ProjectReg reg){
        return String.valueOf(projectService.pathModify(code,reg).getCode());
    }
}
