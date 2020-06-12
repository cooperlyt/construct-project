package cc.coopersoft.construct.corp.controllers;

import cc.coopersoft.construct.corp.model.CorpBusiness;
import cc.coopersoft.construct.corp.model.CorpEmployee;
import cc.coopersoft.construct.corp.services.CorpServices;
import cc.coopersoft.construct.corp.services.EmployeeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(value="mgr")
public class ManagerController {

    private final CorpServices corpServices;

    private final EmployeeServices employeeServices;

    @Autowired
    public ManagerController(CorpServices corpServices, EmployeeServices employeeServices) {
        this.corpServices = corpServices;
        this.employeeServices = employeeServices;
    }


    @RequestMapping(value = "/path/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathCreate(@Valid @RequestBody CorpBusiness regBusiness){
        return String.valueOf(this.corpServices.patchCreate(regBusiness).getCode());
    }


    @RequestMapping(value = "/path/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathModify(
            @PathVariable("code") long corpCode,
            @Valid @RequestBody CorpBusiness regBusiness){
        return String.valueOf(this.corpServices.patchModify(corpCode,regBusiness).getCode());
    }

    @RequestMapping(value = "/enable/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String enableCorp(@PathVariable("code") Long corpCode){
        this.corpServices.setCorpEnable(corpCode,true);
        return "{ \"code\":" +  corpCode + " , \"enable\":true}" ;
    }

    @RequestMapping(value = "/disable/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String disableCorp(@PathVariable("code") long corpCode){
        this.corpServices.setCorpEnable(corpCode,false);
        return "{ \"code\":" +  corpCode + " , \"enable\":false}" ;
    }

    @RequestMapping(value = "/corp/{code}/employee/add", method = RequestMethod.POST)
    public String addCorpEmployee(@PathVariable("code") long corpCode, @Valid @RequestBody CorpEmployee employee){
        return String.valueOf(employeeServices.addEmployee(corpCode,employee).getId());
    }

    @RequestMapping(value = "/employee/{code}", method = RequestMethod.GET)
    public CorpEmployee getEmployee(@PathVariable("code") long code){
        return employeeServices.employee(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
