package com.misaulasunq.utils;

import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;

import java.util.ArrayList;


public class DegreeBuilder {

    private final Degree degree;

    private DegreeBuilder(){
        this.degree  = new Degree();
    }

    public static DegreeBuilder buildADegree(){
        return new DegreeBuilder();
    }

    public DegreeBuilder withMockData(){
        this.degree.setSubjects(new ArrayList<>());
        this.degree.setName("Test Degree");
        return this;
    }

    public Degree build(){
        return this.degree;
    }
}
