package com.calculator.dataentry.common;

import com.calculator.dataentry.model.SingleUserModel;
import com.calculator.dataentry.model.UserModel;
import com.calculator.dataentry.model.formDataModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("register")
    Call<formDataModel>register(@Header("Authorization") String token,@Field("name") String name, @Field("mobile") String mobile, @Field("email") String email, @Field("type") String Type);

    @FormUrlEncoded
    @POST("updateUserDetails")
    Call<formDataModel>registerUpdate(@Header("Authorization") String token,@Field("id") String id,@Field("name") String name, @Field("mobile") String mobile, @Field("email") String email);

    @FormUrlEncoded
    @POST("deleteUser")
    Call<formDataModel>registerDelete(@Header("Authorization") String token,@Field("id") String id,@Field("status") String status);

    @FormUrlEncoded
    @POST("insertHistory")
    Call<formDataModel>insertHistory(@Header("Authorization") String token,@Field("finaldate") String finaldate,@Field("user_id") String user_id, @Field("particulars") String particulars, @Field("debit_amount") String debit_amount, @Field("credit_amount") String credit_amount);

    @FormUrlEncoded
    @POST("getAllUsers")
    Call<UserModel>getAllUserData(@Header("Authorization") String token,@Field("type") String type,@Field("status") String status);

    @FormUrlEncoded
    @POST("getSingleUserDetails")
    Call<SingleUserModel>getSingleUserDetails(@Header("Authorization") String token,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("eventlist")
    Call<SingleUserModel>getEventList(@Header("Authorization") String token,@Field("event_date") String user_id, @Field("delete_status") String delet_status);

    @FormUrlEncoded
    @POST("eventDelete")
    Call<SingleUserModel>getDeletEvent(@Header("Authorization") String token,@Field("id") String id,@Field("delete_status") String delete_status);

    @FormUrlEncoded
    @POST("getImage")
    Call<SingleUserModel>getImage(@Header("Authorization") String token,
                                  @Field("user_id") String user_id,@Field("delete_status") String delete_status);

    @FormUrlEncoded
    @POST("imageDelete")
    Call<SingleUserModel>getDeletImage(@Header("Authorization") String token,
                                       @Field("id") String id,
                                       @Field("image") String image,
                                       @Field("delete_status") String delete_status,
                                       @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("deleteHistoryData")
    Call<formDataModel>getDeleteDataHistory(@Header("Authorization") String token,@Field("id") String user_id);

    @FormUrlEncoded
    @POST("eventInsert")
    Call<SingleUserModel>getInsert_Event(@Header("Authorization") String token,@Field("event_title") String event_title, @Field("event_date") String event_date);

    @FormUrlEncoded
    @POST("insertImage")
    Call<SingleUserModel>getUploadImage(@Header("Authorization") String token,@Field("user_id") String user_id, @Field("image") String image);

    @FormUrlEncoded
    @POST("eventUpdate")
    Call<SingleUserModel>getupdateEvent(@Header("Authorization") String token,@Field("event_title") String event_title,
                                        @Field("event_status") String event_status,
                                        @Field("event_date") String event_date,
                                        @Field("id") String id);

    @FormUrlEncoded
    @POST("updateHistoryData")
    Call<formDataModel>getupdateHistory(@Header("Authorization") String token,@Field("finaldate") String finaldate,@Field("particulars") String particulars,
                                        @Field("debit_amount") String event_title,
                                        @Field("credit_amount") String event_status,
                                        @Field("user_id") String id);

    @FormUrlEncoded
    @POST("login")
    Call<SingleUserModel>getlogin(@Field("username") String name,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("changepassword")
    Call<SingleUserModel>getchangePassword(@Header("Authorization") String token,@Field("id") String id,@Field("password") String password);


    @FormUrlEncoded
    @POST("eventlistrecyclerbin")
    Call<SingleUserModel>getReCalender(@Header("Authorization") String token,@Field("delete_status") String delete_status);


    @FormUrlEncoded
    @POST("imagelistrecyclerbin")
    Call<UserModel>getGallery(@Header("Authorization") String token,
                                    @Field("delete_status") String delete_status);



    @FormUrlEncoded
    @POST("deleteImageUsers")
    Call<formDataModel>DeletGallery(@Header("Authorization") String token,
                              @Field("user_id") String user_id,@Field("status") String status);


    @FormUrlEncoded
    @POST("searchEvent")
    Call<SingleUserModel>SearchEvent(@Header("Authorization") String token,
                                    @Field("event_title") String event_title);



}
