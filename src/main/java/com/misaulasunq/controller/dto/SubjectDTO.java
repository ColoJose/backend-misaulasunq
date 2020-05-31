package com.misaulasunq.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.misaulasunq.model.Commission;
import com.misaulasunq.model.Degree;
import com.misaulasunq.model.Subject;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectDTO {

    protected Integer id;
    protected String name;
    protected String subjectCode;
    protected List<CommissionDTO> commissions;
    protected List<DegreeDTO> degrees;

    public SubjectDTO(Subject subject){
        this.id = subject.getId();
        this.name = subject.getName();
        this.subjectCode = subject.getSubjectCode();
        this.commissions = this.createCommissionsDTO(subject.getCommissions());
        this.degrees = this.createDegreeDTO(subject.getDegrees());
    }

    private List<DegreeDTO> createDegreeDTO(List<Degree> degree) {
        return degree.stream().map(DegreeDTO::new).collect(Collectors.toList());
    }

    private List<CommissionDTO> createCommissionsDTO(List<Commission> commissions) {
        return commissions.stream().map(cm -> new CommissionDTO(cm)).collect(Collectors.toList());
    }

    public Integer getId() {    return id;  }
    public String getName() {   return name;    }
    public String getSubjectCode() {    return subjectCode; }
    public List<CommissionDTO> getCommissions() {   return commissions; }
    public List<DegreeDTO> getDegrees() {   return degrees; }

}
