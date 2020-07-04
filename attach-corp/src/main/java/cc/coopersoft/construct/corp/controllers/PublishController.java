package cc.coopersoft.construct.corp.controllers;


import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.services.CorpServices;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

@RestController
@RequestMapping(value="publish")
public class PublishController {

    private final CorpServices corpServices;

    public PublishController(CorpServices corpServices) {
        this.corpServices = corpServices;
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

}
