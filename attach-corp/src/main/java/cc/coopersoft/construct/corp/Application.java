package cc.coopersoft.construct.corp;

import cc.coopersoft.construct.corp.services.CorpChangeChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringCloudApplication
@EnableBinding(CorpChangeChannel.class)
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
