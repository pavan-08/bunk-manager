package com.bunkmanager.entity;

/**
 * Created by Pavan on 6/17/2016.
 */
public class BunkPlanner {
    int _id;
    String date;
    int status;
    boolean expanded;

    public BunkPlanner() {
        expanded = false;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void toggleExpanded() {
        this.expanded = !this.expanded;
    }
}
