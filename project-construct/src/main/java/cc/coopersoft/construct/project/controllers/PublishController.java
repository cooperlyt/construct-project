package cc.coopersoft.construct.project.controllers;


import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.services.ProjectServices;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value="publish")
public class PublishController {

    private final ProjectServices projectServices;

    public PublishController(ProjectServices projectServices) {
        this.projectServices = projectServices;
    }

    @RequestMapping(value = "/project/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Project.Details.class)
    public Project project(@PathVariable("code") long code){
        Optional<Project> result = projectServices.project(code);
        if (result.isPresent()){
            return result.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


}
