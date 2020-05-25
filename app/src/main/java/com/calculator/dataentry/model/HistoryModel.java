package com.calculator.dataentry.model;

public class HistoryModel {
    private String Date;
    private String perticular;
    private String current_amount;
    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getCurrent_amount() {
        return current_amount;
    }

    public void setCurrent_amount(String current_amount) {
        this.current_amount = current_amount;
    }

    public String getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(String debit_amount) {
        this.debit_amount = debit_amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPerticular() {
        return perticular;
    }

    public void setPerticular(String perticular) {
        this.perticular = perticular;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private String debit_amount;
    private String status;
    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String balance;
}
