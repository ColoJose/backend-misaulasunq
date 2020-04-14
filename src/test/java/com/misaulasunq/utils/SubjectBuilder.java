package com.misaulasunq.utils;

import com.misaulasunq.model.Commission;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;

import java.util.Set;

public class SubjectBuilder {

    private Subject subject;

    private SubjectBuilder(){
        this.subject  = new Subject();
    }

    public static SubjectBuilder buildASubject(){
        return new SubjectBuilder();
    }

    public SubjectBuilder withName(String name){
        this.subject.setName(name);
        return this;
    }

    public SubjectBuilder withDegrees(Set<Degree> degrees){
        this.subject.setDegrees(degrees);
        return this;
    }

    public SubjectBuilder withSubjectCode(String code){
        this.subject.setSubjectCode(code);
        return this;
    }

    public SubjectBuilder withCommissions(Set<Commission> commissions){
        this.subject.setCommissions(commissions);
        return this;
    }

    public Subject build(){
        return this.subject;
    }
}
