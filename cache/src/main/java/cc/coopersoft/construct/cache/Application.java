package cc.coopersoft.construct.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;



@SpringCloudApplication
@EnableResourceServer
public class Application {


    private final ServiceConfig serviceConfig;

    @Autowired
    public Application(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(serviceConfig.getRedisServer(), serviceConfig.getRedisPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(serviceConfig.getRedisPassword()));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
