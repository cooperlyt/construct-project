package cc.coopersoft.construct.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages = {"cc.coopersoft.common.cloud","cc.coopersoft.construct.project"})
@EnableEurekaClient
@EnableResourceServer
@EnableBinding(Source.class)
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

}
