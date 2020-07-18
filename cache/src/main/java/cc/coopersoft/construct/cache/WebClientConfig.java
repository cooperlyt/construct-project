package cc.coopersoft.construct.cache;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder builder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactiveClientRegistrationRepository clientRegistrationRepository,
                               ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        oauth.setDefaultClientRegistrationId("master-extend-cer");


        return builder.filter(oauth).build();
    }
//
//
//    @Bean
//    ReactiveOAuth2AuthorizedClientManager authorizedClientManager(ReactiveClientRegistrationRepository clientRegistrationRepository,
//                                                                  ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                        .clientCredentials()
//                        .build();
//
//        if (authorizedClientRepository instanceof UnAuthenticatedServerOAuth2AuthorizedClientRepository) {
//            ServerOAuth2AuthorizedClientExchangeFilterFunction.UnAuthenticatedReactiveOAuth2AuthorizedClientManager unauthenticatedAuthorizedClientManager = new ServerOAuth2AuthorizedClientExchangeFilterFunction.UnAuthenticatedReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, (UnAuthenticatedServerOAuth2AuthorizedClientRepository)authorizedClientRepository);
//            unauthenticatedAuthorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//            return unauthenticatedAuthorizedClientManager;
//        } else {
//            DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
//            authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//            return authorizedClientManager;
//        }
//
//
//        ServerOAuth2AuthorizedClientExchangeFilterFunction.
//
//        ReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
//                clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//        return authorizedClientManager;
//    }

}
