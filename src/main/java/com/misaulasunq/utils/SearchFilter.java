package com.misaulasunq.utils;

public enum SearchFilter {

    BY_SUBJECT("bySubject"),
    BY_SCHEDULE("bySchedule"),
    BY_CLASSROOM("byClassroom"),
    BY_DAY("byDay");

    private final String searchFilter;

    SearchFilter(String searchFilter) {   this.searchFilter = searchFilter; }
    public String getSearchFilter() {    return searchFilter; }
}
