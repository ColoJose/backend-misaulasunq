package com.misaulasunq.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/subjectAPI/edit*").authenticated()
                .antMatchers("/subjectAPI/new-subject").authenticated()
                .antMatchers("/subjectAPI/commissions/{id}").authenticated()
                .anyRequest().permitAll();
    }
}
