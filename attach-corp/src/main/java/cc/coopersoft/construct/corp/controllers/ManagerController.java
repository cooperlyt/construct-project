package cc.coopersoft.construct.corp.controllers;


import cc.coopersoft.common.data.ConstructJoinType;
import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpBusiness;
import cc.coopersoft.construct.corp.services.CorpServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="mgr")
public class ManagerController {

    private final CorpServices corpServices;

    @Autowired
    public ManagerController(CorpServices corpServices) {
        this.corpServices = corpServices;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<Corp> listCorp(@RequestParam("valid") boolean valid,
                               @RequestParam(value = "joinType", required = false) Optional<ConstructJoinType> joinType,
                               @RequestParam(value ="page", required = false) Optional<Integer> page,
                               @RequestParam(value ="key", required = false)Optional<String> key,
                               @RequestParam(value ="sort", required = false)Optional<String> sort,
                               @RequestParam(value ="dir", required = false)Optional<String> dir){
        return corpServices.listAllCorp(valid,joinType,page,key,sort,dir);
    }

    @RequestMapping(value = "/corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Corp corp(@PathVariable("code") String corpCode){
        Optional<Corp> _corp = this.corpServices.corp(corpCode);
        if (_corp.isPresent()){
            return _corp.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/business/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CorpBusiness business(@PathVariable("id") long id){
        Optional<CorpBusiness> _business = this.corpServices.corpBusiness(id);
        if (_business.isPresent()){
            return _business.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/corp/{code}/business", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CorpBusiness> listBusiness(@PathVariable("code") String corpCode){
        return this.corpServices.listBusiness(corpCode);
    }

    @RequestMapping(value = "/path/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CorpBusiness pathCreate(@Valid @RequestBody CorpBusiness regBusiness){
        return this.corpServices.patchCreate(regBusiness);
    }


    @RequestMapping(value = "/path/modify/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CorpBusiness pathModify(
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
