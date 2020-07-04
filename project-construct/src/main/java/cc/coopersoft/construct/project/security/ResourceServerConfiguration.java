package cc.coopersoft.construct.project.security;

import cc.coopersoft.common.cloud.keycloak.KeycloakAuthenticationConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/publish/**").permitAll()
                .antMatchers("/mgr/**").hasRole("DATA.MGR")
                .antMatchers("/view/**").hasAuthority("SCOPE_Master")
                .antMatchers("/trust/**").hasAuthority("SCOPE_Trust")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(new KeycloakAuthenticationConverter("master"));

    }

}
