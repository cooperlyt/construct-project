package cc.coopersoft.construct.cache.controllers;


import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.construct.cache.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value="data")
public class CommonData {

    private final CacheService cacheService;

    @Autowired
    public CommonData(CacheService cacheService) {
        this.cacheService = cacheService;
    }


    @RequestMapping(value = "/corp/{code}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Corp.Default corp(@PathVariable("code") long code){
        Corp.Default result = cacheService.getCorp(code);
        if (result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return result;
    }



}
