package cc.coopersoft.trust.gateway.security;

import cc.coopersoft.trust.gateway.services.UserValidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final String ORG_ID_PARAM_NAME = "org";

    private final UserValidService userValidService;

    public AuthFilter(UserValidService userValidService) {
        this.userValidService = userValidService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String org = exchange.getRequest().getQueryParams().getFirst(ORG_ID_PARAM_NAME);

        log.debug( "filter form org param: " + org);
        if (StringUtils.isEmpty(org)){
            return chain.filter(exchange);
        }else{
            return ReactiveSecurityContextHolder.getContext()
                    .flatMap(context -> (transform(context,org))).flatMap(r -> {
                        if (r){
                            log.debug("corp check pass :" + org);
                            return chain.filter(exchange);
                        }else{
                            log.debug("no corp auth :" + org);
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                            return exchange.getResponse().setComplete();
                        }
                    });
        }

    }

    private Mono<Boolean> transform(SecurityContext securityContext, String org) {
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt){
                Jwt jwt = (Jwt) principal;
                String username = jwt.getClaimAsString("preferred_username");
                return userValidService.get(username).map(c -> c.containsKey(org));
            }else{
               log.warn("principal is not jwt");
            }

        }

        return Mono.just(false);

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
