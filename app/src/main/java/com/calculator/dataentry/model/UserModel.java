package com.calculator.dataentry.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserModel {

    @SerializedName("status")
    public String status;

    @SerializedName("result")
    public ArrayList<userDetails> userList = new ArrayList<>();

    public static class userDetails {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("email")
        public String email;

        @SerializedName("totalamount")
        public String totalamount;

        @SerializedName("datetime")
        public String datetime;

    }
}
