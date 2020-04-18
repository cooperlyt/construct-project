package cc.coopersoft.construct.cache.controllers;


import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.project.Project;
import cc.coopersoft.construct.cache.services.CorpCacheService;
import cc.coopersoft.construct.cache.services.ProjectCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value="data")
public class CommonData {

    private final CorpCacheService corpCacheService;

    private final ProjectCacheService projectCacheService;

    @Autowired
    public CommonData(CorpCacheService corpCacheService,
                      ProjectCacheService projectCacheService) {
        this.corpCacheService = corpCacheService;
        this.projectCacheService = projectCacheService;
    }


    @RequestMapping(value = "/corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Corp.Default corp(@PathVariable("code") long code){
        Corp.Default result = corpCacheService.get(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return result;
    }

    @RequestMapping(value = "/project/{code}", method = RequestMethod.GET)
    public Project.Default project(@PathVariable("code") long code){
        Project.Default result = projectCacheService.get(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

}
