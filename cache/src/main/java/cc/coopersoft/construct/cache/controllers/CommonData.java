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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serverExceptionHandler(Exception ex) {
        //LOGGER.error(ex.getMessage(),ex);
        return ex.getMessage();
    }

    @RequestMapping(value = "/corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Corp.Default> corp(@PathVariable("code") long code){
        return corpCacheService.get(code);
    }

    @RequestMapping(value = "/project/{code}", method = RequestMethod.GET)
    public Mono<Project.Default> project(@PathVariable("code") long code){
        return projectCacheService.get(code);
    }


//    @RequestMapping(value = "/project-corp/{code}", method = RequestMethod.GET)
//    @ResponseStatus(HttpStatus.OK)
//    public Mono<DataService.ProjectAndCorp> projectAndCorp(@PathVariable("code") long code){
//        DataService.ProjectAndCorp result = dataService.projectAndCorp(code);
//        if (result == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        return Mono.just(result);
//    }
//
//    @RequestMapping(value = "/project/{code}/corps", method = RequestMethod.GET)
//    public Flux<JoinCorp<JoinCorpInfo>> projectCorps(@PathVariable("code") long code){
//        Project.Default result = projectCacheService.get(code);
//        if (result == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//
//        return Flux.fromIterable(result.getCorps());
//    }
//
//    @RequestMapping(value = "/project/{code}/corp/reg", method = RequestMethod.GET)
//    public Flux<DataService.JoinCorpAndCorp> projectCorpRegs(@PathVariable("code") long code){
//        return Flux.fromIterable(dataService.joinCorpAndCorp(code));
//    }

    @RequestMapping(value = "/project/{code}/info", method = RequestMethod.GET)
    public Mono<ProjectRegInfo> projectInfo(@PathVariable("code") long code){
        return projectCacheService.get(code).map(Project.Default::getInfo);
    }

    @RequestMapping(value = "/project/{code}/builds", method = RequestMethod.GET)
    public Flux<BuildRegInfo<BuildInfo>> projectBuilds(@PathVariable("code") long code){
        return projectCacheService.get(code).map(Project.Default::getBuilds).flatMapMany(Flux::fromIterable);
    }

}
