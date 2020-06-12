package cc.coopersoft.construct.corp.controllers;


import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpBusiness;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="publish")
public class PublishController {


    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public String status(){
        return "Running";
    }

}
