package cc.coopersoft.construct.corp.controllers;

import cc.coopersoft.construct.corp.model.CorpBusiness;
import cc.coopersoft.construct.corp.services.CorpServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="mgr")
public class ManagerController {

    private final CorpServices corpServices;

    @Autowired
    public ManagerController(CorpServices corpServices) {
        this.corpServices = corpServices;
    }


    @RequestMapping(value = "/path/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathCreate(@Valid @RequestBody CorpBusiness regBusiness){
        return String.valueOf(this.corpServices.patchCreate(regBusiness).getCorpCode());
    }


    @RequestMapping(value = "/path/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathModify(
            @PathVariable("code") long corpCode,
            @Valid @RequestBody CorpBusiness regBusiness){
        return String.valueOf(this.corpServices.patchModify(corpCode,regBusiness).getCorpCode());
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


}
