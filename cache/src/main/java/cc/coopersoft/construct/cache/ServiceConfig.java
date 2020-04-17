package cc.coopersoft.construct.cache;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {

    @Value("${cc.coopersoft.construct.redis.server}")
    private String redisServer;

    @Value("${cc.coopersoft.construct.redis.port}")
    private int redisPort;

    @Value("${cc.coopersoft.construct.redis.password}")
    private String redisPassword;

    public String getRedisPassword() {
        return redisPassword;
    }

    public String getRedisServer() {
        return redisServer;
    }

    public int getRedisPort() {
        return redisPort;
    }
}
