package cc.coopersoft.construct.corp.controllers;

import cc.coopersoft.common.data.ConstructJoinType;
import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpBusiness;
import cc.coopersoft.construct.corp.services.CorpServices;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("view")
@Slf4j
public class ViewController {

    private final CorpServices corpServices;

    @Autowired
    public ViewController(CorpServices corpServices) {
        this.corpServices = corpServices;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Corp.Summary.class)
    public Page<Corp> listCorp(@RequestParam(value = "valid", required = false) Optional<Boolean> valid,
                               @RequestParam(value = "type", required = false) Optional<ConstructJoinType> joinType,
                               @RequestParam(value ="page", required = false) Optional<Integer> page,
                               @RequestParam(value ="key", required = false)Optional<String> key,
                               @RequestParam(value ="sort", required = false)Optional<String> sort,
                               @RequestParam(value ="dir", required = false)Optional<String> dir){
        return corpServices.listAllCorp(valid,joinType,page,key,sort,dir);
    }

    @RequestMapping(value = "/corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Corp.Details.class)
    public Corp corp(@PathVariable("code") long corpCode){
        Optional<Corp> _corp = this.corpServices.corp(corpCode);
        if (_corp.isPresent()){
            return _corp.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/business/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(CorpBusiness.Details.class)
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
    @JsonView(CorpBusiness.Summary.class)
    public List<CorpBusiness> listBusiness(@PathVariable("code") long corpCode){
        return this.corpServices.listBusiness(corpCode);
    }

    @RequestMapping(value = "/corp/exists/{type}/{number}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String existsCorpGroupNumber(@PathVariable("type") GroupIdType type,
                                        @PathVariable("number") String number){
        return String.valueOf(corpServices.existsCorpGroupNumber(type,number));
    }

    @RequestMapping(value = "/corp/exists/{type}/{number}/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String existsCorpGroupNumber(@PathVariable("code") long code,
                                        @PathVariable("type") GroupIdType type,
                                        @PathVariable("number") String number){
        return String.valueOf(corpServices.existsCorpGroupNumber(code,type,number));
    }

    @RequestMapping(value = "/business/exists/corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String corpInBusiness(@PathVariable("code") long code){
        return String.valueOf(corpServices.corpInBusiness(code));

    }

}
