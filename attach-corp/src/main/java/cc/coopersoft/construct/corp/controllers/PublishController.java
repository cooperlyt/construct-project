package cc.coopersoft.construct.corp.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="publish")
public class PublishController {


    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public String status(){
        return "Running";
    }

}
