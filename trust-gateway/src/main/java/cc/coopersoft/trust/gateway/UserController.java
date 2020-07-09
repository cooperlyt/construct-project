package cc.coopersoft.trust.gateway;


import cc.coopersoft.trust.gateway.services.UserValidService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class UserController {

    private final UserValidService userValidService;

    public UserController(UserValidService userValidService) {
        this.userValidService = userValidService;
    }

    @RequestMapping(value = "/corps",method = RequestMethod.GET)
    public Mono<Map<String,String>> corps(){
        return ReactiveSecurityContextHolder.getContext().flatMap(this::transform);
    }

    private Mono<Map<String,String>> transform(SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;
                String username = jwt.getClaimAsString("preferred_username");
                return userValidService.get(username);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    }

}
