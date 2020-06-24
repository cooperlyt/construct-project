package cc.coopersoft.construct.project.controllers;

import cc.coopersoft.construct.project.model.ProjectReg;
import cc.coopersoft.construct.project.services.BusinessService;
import cc.coopersoft.construct.project.services.ProjectServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="mgr")
public class ManagerController {

    private final BusinessService businessService;

    private final ProjectServices projectServices;

    @Autowired
    public ManagerController(BusinessService businessService, ProjectServices projectServices) {
        this.businessService = businessService;
        this.projectServices = projectServices;
    }

    @RequestMapping(value = "/enable/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String enable(@PathVariable("code") long code){
        this.projectServices.enableProject(code,true);
        return "{ \"code\":" +  code + " , \"enable\":true}" ;
    }

    @RequestMapping(value = "/disable/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String disable(@PathVariable("code") long code){
        this.projectServices.enableProject(code,false);
        return "{ \"code\":" +  code + " , \"enable\":false}" ;
    }



    @RequestMapping(value = "/patch/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String patchCreate(@Valid @RequestBody  ProjectReg reg){
        return String.valueOf(businessService.patchCreate(reg).getCode());
    }

    @RequestMapping(value = "/patch/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathModify(@PathVariable("code") long code, @RequestBody @Valid ProjectReg reg){
        return String.valueOf(businessService.pathModify(code,reg).getCode());
    }
}
