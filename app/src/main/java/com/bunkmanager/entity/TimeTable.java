package com.bunkmanager.entity;

/**
 * Created by Pavan on 2/24/2016.
 */
public class TimeTable {
    int id;
    String day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Subjects getSubject() {
        return subject;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
    }

    Subjects subject;
}
