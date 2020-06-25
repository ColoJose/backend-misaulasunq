package com.misaulasunq.utils;

import com.misaulasunq.model.Classroom;
import com.misaulasunq.model.OverlapNotice;
import com.misaulasunq.model.Schedule;

import java.util.List;

public class OverlapNoticeBuilder {

    private final OverlapNotice notice;

    private OverlapNoticeBuilder(){
        this.notice  = new OverlapNotice();
    }

    public static OverlapNoticeBuilder buildAOverlapNotice(){
        return new OverlapNoticeBuilder();
    }

    public OverlapNoticeBuilder withScheduleConflicted(Schedule schedule){
        this.notice.setScheduleConflcited(schedule);
        return this;
    }

    public OverlapNoticeBuilder withClassroom(Classroom classroom) {
        this.notice.setClassroom(classroom);
        return this;
    }

    public OverlapNoticeBuilder withScheduleAffected(Schedule schedule){
        this.notice.setScheduleAffected(schedule);
        return this;
    }

    public OverlapNotice build(){
        return this.notice;
    }
}