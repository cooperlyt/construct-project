package cc.coopersoft.construct.project.controllers;

import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.services.TrustService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="trust")
public class TrustController {

    private static final String TRUST_ROLE_PREFIX = "T_";

    private final TrustService trustService;

    public TrustController(TrustService trustService) {
        this.trustService = trustService;
    }

    private boolean hasCorpRole(long corp){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(res -> {if (res.startsWith("ROLE_"))  return res.substring(5);  else return res; }) // Strip "ROLE_"
                .collect(Collectors.toSet()).contains(TRUST_ROLE_PREFIX + corp);
    }

    @RequestMapping(value = "{corp}/projects", method = RequestMethod.GET)
    @JsonView(Project.Summary.class)
    public List<Project> listProject(@PathVariable("corp") long corp,
                                     @RequestParam(value = "property", required = false) CorpProperty property,
                                     @RequestParam(value = "key", required = false) String key){
        if (hasCorpRole(corp)){
            return trustService.searchProject(corp,key,property);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

    }

}
