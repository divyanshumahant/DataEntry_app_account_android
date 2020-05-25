package com.calculator.dataentry.model;

public class AddEventModel {
    String id,name,date,status;

    public AddEventModel() {
    }

    public AddEventModel(String id, String name, String date, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
