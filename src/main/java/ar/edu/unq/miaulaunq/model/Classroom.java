package ar.edu.unq.miaulaunq.model;

import java.util.List;

public class Classroom {

    private Integer id;
    private Integer number;
    private List<Schedule> schedules;
    private String imageOnBase64;

    public Classroom(){}

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getImageOnBase64() {
        return imageOnBase64;
    }

    public void setImageOnBase64(String imageOnBase64) {
        this.imageOnBase64 = imageOnBase64;
    }
}