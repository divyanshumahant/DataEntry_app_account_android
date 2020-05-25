package com.calculator.dataentry.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SingleUserModel {

    @SerializedName("status")
    public String status;


    @SerializedName("token")
    public String token;

    @SerializedName("credittotal")
    public String credittotal;

    @SerializedName("imageUrl")
    public String imageUrl;

    @SerializedName("balance_amount")
    public String balance_amount;

    @SerializedName("data")
    public JsonObject data;

    @SerializedName("result")
    public ArrayList<userDetails> userList = new ArrayList<>();

    public static class userDetails {

        @SerializedName("id")
        public String id;

        @SerializedName("image")
        public String image;

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("particulars")
        public String particulars;

        @SerializedName("type")
        public String type;

        @SerializedName("invoiceno")
        public String invoiceno;

        @SerializedName("finaldate")
        public String finaldate;

        @SerializedName("debit_amount")
        public String debit_amount;

        @SerializedName("credit_amount")
        public String credit_amount;

        @SerializedName("balance_amount")
        public String balance_amount;

        @SerializedName("status")
        public String status;

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("event_title")
        public String event_title;

        @SerializedName("event_date")
        public String event_date;

        @SerializedName("event_status")
        public String event_status;

    }
}
