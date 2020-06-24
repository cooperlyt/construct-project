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
@RequestMapping(value="path")
public class ManagerController {

    private final CorpServices corpServices;

    public ManagerController(CorpServices corpServices) {
        this.corpServices = corpServices;
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathCreate(@Valid @RequestBody CorpBusiness regBusiness){
        return String.valueOf(this.corpServices.patchCreate(regBusiness).getCode());
    }


    @RequestMapping(value = "/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pathModify(
            @PathVariable("code") long corpCode,
            @Valid @RequestBody CorpBusiness regBusiness){
        return String.valueOf(this.corpServices.patchModify(corpCode,regBusiness).getCode());
    }



}
