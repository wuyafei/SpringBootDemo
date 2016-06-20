package me.fly.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyafei on 16/6/20.
 */
@Configuration
@ConfigurationProperties(prefix="lss")
public class Servers {
    private List<String> sessions = new ArrayList<>();

    public List<String> getSessions() {
        return this.sessions;
    }
}
