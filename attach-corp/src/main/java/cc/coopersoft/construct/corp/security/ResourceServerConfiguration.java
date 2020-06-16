package cc.coopersoft.construct.corp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@Order(1)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/publish/**").permitAll()
                .antMatchers("/path/**").hasAuthority("DATA_MGR")
                .antMatchers("/mgr/**").hasAuthority("CONSTRUCT.CORP")
                .antMatchers("/view/**").hasAuthority("Master")
                .antMatchers("/trust/**").hasAuthority("Trust")
                .anyRequest().authenticated();

    }

}
