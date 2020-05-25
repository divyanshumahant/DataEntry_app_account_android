package com.calculator.dataentry.model;

public class EventModel {

    String name,date,time,description,location,priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public EventModel(String name, String date, String time, String description, String location, String priority) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.description = description;
        this.location = location;
        this.priority = priority;
    }

    public EventModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
