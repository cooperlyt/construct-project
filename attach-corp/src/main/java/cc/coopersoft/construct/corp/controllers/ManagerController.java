package cc.coopersoft.construct.corp.controllers;


import cc.coopersoft.construct.corp.model.Corp;
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
    public Corp pathCreate(@Valid @RequestBody CorpBusiness regBusiness){
        return this.corpServices.patchCreate(regBusiness);
    }


    @RequestMapping(value = "/path/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Corp pathModify(
            @PathVariable("code") String corpCode,
            @Valid @RequestBody CorpBusiness regBusiness){
        return this.corpServices.patchModify(corpCode,regBusiness);
    }

    @RequestMapping(value = "/enable/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void enableCorp(@PathVariable("code") String corpCode){
        this.corpServices.setCorpEnable(corpCode,true);
    }

    @RequestMapping(value = "/disable/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void disableCorp(@PathVariable("code") String corpCode){
        this.corpServices.setCorpEnable(corpCode,false);
    }


}
