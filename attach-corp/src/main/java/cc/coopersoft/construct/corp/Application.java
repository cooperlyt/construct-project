package cc.coopersoft.construct.corp;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@SpringCloudApplication
@EnableBinding(Source.class)
public class Application {



    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
