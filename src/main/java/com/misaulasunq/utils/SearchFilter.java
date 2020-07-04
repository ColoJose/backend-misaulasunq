package com.misaulasunq.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SearchFilter {
    @JsonProperty("bySubject") BY_SUBJECT("bySubject"),
    @JsonProperty("bySchedule") BY_SCHEDULE("bySchedule"),
    @JsonProperty("byClassroom") BY_CLASSROOM("byClassroom"),
    @JsonProperty("byDay") BY_DAY("byDay");

    private final String searchFilter;

    SearchFilter(String searchFilter) {   this.searchFilter = searchFilter; }
    public String getSearchFilter() {    return searchFilter; }
}
