package cc.coopersoft.construct.project.controllers;


import cc.coopersoft.construct.project.model.JoinCorp;
import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectInfoReg;
import cc.coopersoft.construct.project.services.ProjectService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="view")
public class ViewController {

    private final ProjectService projectService;

    @Autowired
    public ViewController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Project.Summary.class)
    public Page<Project> projects(@RequestParam(value = "valid", required = false) Optional<Boolean> valid,
                                  @RequestParam(value="property", required = false) Optional<ProjectInfoReg.PropertyClass> property,
                                  @RequestParam(value="class", required = false) Optional<ProjectInfoReg.ProjectClass> projectClass,
                                  @RequestParam(value = "important", required = false) Optional<ProjectInfoReg.ImportantType> important,
                                  @RequestParam(value ="page", required = false) Optional<Integer> page,
                                  @RequestParam(value ="key", required = false)Optional<String> key,
                                  @RequestParam(value ="sort", required = false)Optional<String> sort,
                                  @RequestParam(value ="dir", required = false)Optional<String> dir){
        return projectService.projects(valid,property,projectClass,important,page,key,sort,dir);
    }


    @RequestMapping(value = "/project/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Project.Details.class)
    public Project project(@PathVariable("code") long code){
        Optional<Project> result = projectService.project(code);
        if (result.isPresent()){
            return result.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/join/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(JoinCorp.SummaryWithCorp.class)
    public List<JoinCorp> joinProjects(@PathVariable("code") long code){
        return projectService.joinProjects(code);
    }


    @RequestMapping(value = "/reg/running/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String corpInReg(@PathVariable("code") long code){
        return String.valueOf(projectService.projectInReg(code));

    }


    
}
