package cc.coopersoft.construct.corp.config;

import cc.coopersoft.common.cloud.security.JWTConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig implements JWTConfig {


    @Value("${signing.key}")
    private String jwtSigningKey="";



    @Override
    public String getJwtSigningKey() {
        return jwtSigningKey;
    }



}
