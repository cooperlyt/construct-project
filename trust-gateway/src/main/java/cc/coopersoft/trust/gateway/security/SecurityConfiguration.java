package cc.coopersoft.trust.gateway.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.WebClientHttpRoutingFilter;
import org.springframework.cloud.gateway.filter.WebClientWriteResponseFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient loadBalancedWebClient(
            WebClient.Builder loadBalancedWebClientBuilder,
            ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction){
        return loadBalancedWebClientBuilder.filter(filterFunction).build();
    }

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange()
//                 .pathMatchers("/login**", "/error**").permitAll()
                .anyExchange().permitAll().and().csrf().disable()
                .oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter());
//                .jwtAuthenticationConverter(new KeycloakAuthenticationConverter());
        return http.build();
    }

    @Bean
    public WebClientHttpRoutingFilter webClientHttpRoutingFilter(WebClient webClient, ObjectProvider<List<HttpHeadersFilter>> headersFilters) {
        return new WebClientHttpRoutingFilter(webClient, headersFilters);
    }

    @Bean
    public WebClientWriteResponseFilter webClientWriteResponseFilter() {
        return new WebClientWriteResponseFilter();
    }


    @Bean
    public WebClient webClient( ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction) {
        return WebClient.builder().filter(filterFunction).build();
    }


    @Bean
    public ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter(ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                                           ServerOAuth2AuthorizedClientRepository authorizedClientRepository){
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        oauth.setDefaultClientRegistrationId("master-trust-cer");
        return oauth;
    }

}
