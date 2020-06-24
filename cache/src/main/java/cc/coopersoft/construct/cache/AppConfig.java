package cc.coopersoft.construct.cache;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class AppConfig {


    @Bean //RedisConnectionFactory factory
    public RedisTemplate<Long,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<Long,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        RedisSerializer redisSerializer = new StringRedisSerializer();
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);

        JdkSerializationRedisSerializer jdkRedisSerializer=new JdkSerializationRedisSerializer();
        template.setValueSerializer(jdkRedisSerializer);
        template.setHashKeySerializer(jdkRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }



    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @LoadBalanced
    public OAuth2RestTemplate oAuth2RestTemplate(UserInfoRestTemplateFactory factory) {
        return factory.getUserInfoRestTemplate();
    }
}
