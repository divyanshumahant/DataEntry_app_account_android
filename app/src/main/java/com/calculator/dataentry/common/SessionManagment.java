package com.calculator.dataentry.common;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagment {
    // LogCat tag
    private static String TAG = SessionManagment.class.getSimpleName();
    SharedPreferences pref;
    public String mobile;
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "vaishnav Vivah";
    private static final String USER_ID = "id";
    private static final String AUTOMATIC_LOCK = "false";
    private static final String PASSWORD = "0000";
    private static final String LOGIN_STATUS = "false";
    private static final String USER_STATUS = "false";
    private static final String KEY_FCM = "fcmid";
    private static final String KEY_IMAGEPATH = "image";
    private static final String KEY_GENDER = "gender";
    private static final String TOKEN = "token";


    public SessionManagment(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUSER_ID(String s) {
        editor.putString("KEY_ID", s);
        editor.commit();
    }

    public String getUSER_ID() {
        return pref.getString("KEY_ID", USER_ID);
    }

    public void setPassword(String s) {
        editor.putString("PASSWORD", s);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString("PASSWORD", PASSWORD);
    }


    public void setLOGIN_STATUS(String s) {
        editor.putString("LOGIN_STATUS", s);
        editor.commit();
    }

    public String getLOGIN_STATUS() {
        return pref.getString("LOGIN_STATUS", LOGIN_STATUS);
    }


    public void setUSER_STATUS(String s) {
        editor.putString("USER_STATUS", s);
        editor.commit();
    }

    public String getUSER_STATUS() {
        return pref.getString("USER_STATUS", USER_STATUS);
    }


    public void setFCM(String fcm) {
        editor.putString("KEY_FCM", fcm);
        editor.commit();
    }


    public String getFCM() {
        return pref.getString("KEY_FCM", KEY_FCM);
    }

    public void setGENDER(String gen) {
        editor.putString("GENDER", gen);
        editor.commit();
    }


    public String getGENDER() {
        return pref.getString("GENDER", KEY_GENDER);
    }

    public void setIMAGEPATH(String img) {
        editor.putString("IMAGEPATH", img);
        editor.commit();
    }


    public String getIMAGEPATH() {
        return pref.getString("IMAGEPATH", KEY_IMAGEPATH);
    }


    public void setTOKEN(String s) {
        editor.putString("TOKEN", s);
        editor.commit();
    }

    public String getTOKEN() {
        return pref.getString("TOKEN", TOKEN);
    }

}
