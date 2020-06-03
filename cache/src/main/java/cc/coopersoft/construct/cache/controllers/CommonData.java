package cc.coopersoft.construct.cache.controllers;


import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.project.*;
import cc.coopersoft.construct.cache.services.CorpCacheService;
import cc.coopersoft.construct.cache.services.DataService;
import cc.coopersoft.construct.cache.services.ProjectCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value="data")
public class CommonData {

    private final CorpCacheService corpCacheService;

    private final ProjectCacheService projectCacheService;

    private final DataService dataService;

    @Autowired
    public CommonData(CorpCacheService corpCacheService,
                      ProjectCacheService projectCacheService,
                      DataService dataService) {
        this.corpCacheService = corpCacheService;
        this.projectCacheService = projectCacheService;
        this.dataService = dataService;
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


    @RequestMapping(value = "/project-corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DataService.ProjectAndCorp projectAndCorp(@PathVariable("code") long code){
        DataService.ProjectAndCorp result = dataService.projectAndCorp(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @RequestMapping(value = "/project/{code}/corps", method = RequestMethod.GET)
    public List<JoinCorp<JoinCorpInfo>> projectCorps(@PathVariable("code") long code){
        Project.Default result = projectCacheService.get(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return result.getCorps();
    }

    @RequestMapping(value = "/project/{code}/corp/reg", method = RequestMethod.GET)
    public List<DataService.JoinCorpAndCorp> projectCorpRegs(@PathVariable("code") long code){
        return dataService.joinCorpAndCorp(code);
    }

    @RequestMapping(value = "/project/{code}/info", method = RequestMethod.GET)
    public ProjectRegInfo projectInfo(@PathVariable("code") long code){
        Project.Default result = projectCacheService.get(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return result.getInfo();
    }

    @RequestMapping(value = "/project/{code}/builds", method = RequestMethod.GET)
    public List<BuildRegInfo<BuildInfo>> projectBuilds(@PathVariable("code") long code){
        Project.Default result = projectCacheService.get(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return result.getBuilds();
    }

}
