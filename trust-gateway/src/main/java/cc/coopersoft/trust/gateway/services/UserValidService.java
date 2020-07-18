package cc.coopersoft.trust.gateway.services;

import cc.coopersoft.trust.gateway.repository.redis.UserRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

@EnableBinding(Sink.class)
@Slf4j
@Service
public class UserValidService {

    private final UserRedisRepository userRedisRepository;


    private WebClient loadBalancedWebClient;

    public UserValidService(UserRedisRepository userRedisRepository, WebClient loadBalancedWebClient) {
        this.userRedisRepository = userRedisRepository;
        this.loadBalancedWebClient = loadBalancedWebClient;
    }

    public Mono<Map<String,String>> get(String username){
        Map<String,String> corps = userRedisRepository.find(username);
        if (corps != null){
            log.debug(" successfully retrieved a data {} from the cache: {}", username, corps);
            return Mono.just(corps);
        }

        log.debug("Unable to locate data from the cache: {}",username);

        return loadBalancedWebClient.get()
                .uri("http://construct-attach-corp/trust/{username}/corps",username)
                .retrieve()
                .bodyToMono(Map.class)
                .map(c -> {
                    userRedisRepository.save(username,c);
                    return c;
                });



    }

    @StreamListener(Sink.INPUT)
    public void userChange(List<String> users){
        users.forEach(userRedisRepository::delete);
    }
}
