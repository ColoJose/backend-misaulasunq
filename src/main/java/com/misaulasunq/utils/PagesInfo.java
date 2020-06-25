package com.misaulasunq.utils;

import com.misaulasunq.controller.dto.SubjectDTO;

import java.util.List;

public class PagesInfo {

    private List<SubjectDTO> subjectsDTO;
    private Integer nextContentSize;

    public PagesInfo() { }

    public PagesInfo(List<SubjectDTO> subjectPage, Integer nextContentSize) {
        this.subjectsDTO = subjectPage;
        this.nextContentSize = nextContentSize;
    }

    public List<SubjectDTO> getSubjectsDTO() {
        return subjectsDTO;
    }

    public Integer getNextPageSize() {
        return nextContentSize;
    }
}
