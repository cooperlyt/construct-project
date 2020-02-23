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
//                .requestMatchers()
//                .antMatchers("/pos/**","/bank/**")
//                .and()
                .authorizeRequests()
                .antMatchers("/pos/**","/bank/**", "/public/**").permitAll()
                .antMatchers("/developer/**").hasAuthority("DEVELOPER")
                .antMatchers("/gov/**").hasAuthority("HOUSE_GOV")
                .anyRequest().authenticated();

    }

}
