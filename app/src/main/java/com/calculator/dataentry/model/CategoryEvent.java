package com.calculator.dataentry.model;

public class CategoryEvent  {

    public static final int DATE_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    String userid,imageid,image,date;
    int type ;


    public CategoryEvent(String userid, String imageid, String image, String date, int type) {
        this.userid = userid;
        this.imageid = imageid;
        this.image = image;
        this.date = date;
        this.type = type;
    }

    public static int getDateType() {
        return DATE_TYPE;
    }

    public static int getImageType() {
        return IMAGE_TYPE;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
