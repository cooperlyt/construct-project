package cc.coopersoft.construct.project;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringCloudApplication
@EnableWebSecurity
@EnableBinding(Source.class)
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

}
