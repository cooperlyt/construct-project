package cc.coopersoft.construct.corp.controllers;

import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.services.TrustService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="trust")
public class TrustController {

    private final TrustService trustService;

    public TrustController(TrustService trustService) {
        this.trustService = trustService;
    }

//    @RequestMapping(value = "corps", method = RequestMethod.GET)
//    @JsonView(Corp.Title.class)
//    public List<Corp> listCorp(){
//        return trustService.listCorp();
//    }


    @RequestMapping(value = "{username}/corps", method = RequestMethod.GET)
    @JsonView(Corp.Title.class)
    public Map<Long,String> listCorpIds(@PathVariable("username") String username){
        return trustService.listCorp(username).stream().collect(Collectors.toMap(Corp::getCode,c -> c.getInfo().getName()));
    }
}
