package cc.coopersoft.trust.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class KeycloakJwtAuthenticationConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>>{

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private String clientId = null;

    protected Collection<GrantedAuthority> authenticationRoles(Jwt jwt) {
        if (this.clientId == null) {
            this.clientId = jwt.getClaim("azp");
        }

        log.debug("oauth convert: " + jwt.getClaimAsString("preferred_username"));

        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map resource;
        Collection resourceRoles;
        if (resourceAccess != null && this.clientId != null && (resource = (Map)resourceAccess.get(this.clientId)) != null && (resourceRoles = (Collection)resource.get("roles")) != null) {
            authorities.addAll((Collection)resourceRoles.stream().map((x) -> new SimpleGrantedAuthority("ROLE_" + x)).collect(Collectors.toSet()));
        }


        return authorities;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return Mono.just(new JwtAuthenticationToken(jwt, this.authenticationRoles(jwt))) ;
    }
}
