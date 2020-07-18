package cc.coopersoft.construct.corp.controllers;

import cc.coopersoft.construct.corp.model.CorpEmployee;
import cc.coopersoft.construct.corp.model.CreditRecord;
import cc.coopersoft.construct.corp.services.CorpServices;
import cc.coopersoft.construct.corp.services.EmployeeServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="mgr")
public class CorpMasterController {

    private final EmployeeServices employeeServices;
    private final CorpServices corpServices;

    public CorpMasterController(EmployeeServices employeeServices, CorpServices corpServices) {
        this.employeeServices = employeeServices;
        this.corpServices = corpServices;
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

    @RequestMapping(value = "/corp/{code}/credit/add", method = RequestMethod.POST)
    public String addCreditRecord(@PathVariable("code") long corpCode,
                                  @Valid @RequestBody CreditRecord creditRecord){
        return String.valueOf(corpServices.addCredit(corpCode, creditRecord).getId());
    }


}
