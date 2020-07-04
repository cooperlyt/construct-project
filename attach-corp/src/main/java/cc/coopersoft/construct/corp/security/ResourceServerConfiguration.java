package cc.coopersoft.construct.corp.security;

import cc.coopersoft.common.cloud.keycloak.KeycloakAuthenticationConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/view/**")
//                .authorizeRequests()
//                .antMatchers("/view/**").permitAll()
//                .and()
//                .oauth2ResourceServer().jwt();

        http
                .authorizeRequests()
                .antMatchers("/publish/**").permitAll()
                .antMatchers("/path/**").hasRole("DATA.MGR")
                .antMatchers("/mgr/**").hasRole("CONSTRUCT.CORP")
                .antMatchers("/view/**").hasAuthority("SCOPE_Master")
                .antMatchers("/trust/**").hasAuthority("SCOPE_Trust")
                .antMatchers("/actuator/**").permitAll()
                .and()
                .oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(new KeycloakAuthenticationConverter("master"));
    }




}
