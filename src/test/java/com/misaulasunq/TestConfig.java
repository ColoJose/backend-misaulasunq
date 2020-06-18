package com.misaulasunq;

import com.misaulasunq.service.ClassroomService;
import com.misaulasunq.service.DegreeService;
import com.misaulasunq.service.SubjectService;
import com.misaulasunq.service.UploaderService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;

@TestConfiguration // Esta annotation le dice a los test que es una clase de configuracion y no lo agrega al escaneo.
public class TestConfig {

    @Bean
    public UploaderService UploaderService() {
        return new UploaderService();
    }
    @Bean
    public SubjectService SubjectService(){
        return new SubjectService();
    }
    @Bean
    public DegreeService DegreeService(){
        return new DegreeService();
    }
    @Bean
    public ClassroomService ClassroomService(){
        return new ClassroomService();
    }
}
