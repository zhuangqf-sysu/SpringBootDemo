package com.example.zhuangqf.config;

import com.example.zhuangqf.endpoint.StudentEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Created by zhuangqf on 11/18/16.
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig(){
        register(StudentEndpoint.class);
    }

}
