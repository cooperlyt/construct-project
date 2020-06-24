package cc.coopersoft.construct.corp.controllers;

import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.services.TrustService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="trust")
public class TrustController {

    private final TrustService trustService;

    public TrustController(TrustService trustService) {
        this.trustService = trustService;
    }

    @RequestMapping(value = "corps", method = RequestMethod.GET)
    @JsonView(Corp.Title.class)
    public List<Corp> listCorp(){
        return trustService.listCorp();
    }
}
