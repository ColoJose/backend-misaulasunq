package com.misaulasunq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("file:${app.config}/application.properties")
public class AppConfig {

    @Autowired
    Environment environment;

}
