package com.calculator.dataentry.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import com.calculator.dataentry.model.EventModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DataEntry.db";
    public static final String TABLE_NAME = "EventTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Event_name";
    public static final String COL_3 = "Event_date";
    public static final String COL_4 = "Event_time";
    public static final String COL_5 = "Event_description";
    public static final String COL_6 = "Event_location";
    public static final String COL_7 = "Event_priority";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Event_name TEXT," +
                "Event_date TEXT," +
                "Event_time TEXT," +
                "Event_description TEXT," +
                "Event_location TEXT," +
                "Event_priority TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
  }

    public boolean insertEvent(String evname,String evdate, String evtime,
                               String evdescrip, String loc, String prio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, evname);
        contentValues.put(COL_3, evdate);
        contentValues.put(COL_4, evtime);
        contentValues.put(COL_5, evdescrip);
        contentValues.put(COL_6, loc);
        contentValues.put(COL_7, prio);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Event_name = ?", new String[]{name});
    }

    public Integer deletAlldata() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    public boolean updateData(String nam,String evname, String evdate,
                  String evtime, String evdescrip, String loc, String prio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, evname);
        contentValues.put(COL_3, evdate);
        contentValues.put(COL_4, evtime);
        contentValues.put(COL_5, evdescrip);
        contentValues.put(COL_6, loc);
        contentValues.put(COL_7, prio);
        db.update(TABLE_NAME, contentValues, "Event_name = ?", new String[]{nam});
        return true;
    }

    public boolean updatePriority(String nam,String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, priority);
        db.update(TABLE_NAME, contentValues, "Event_name = ?", new String[]{nam});
        return true;
    }


    public ArrayList<EventModel> getAllcartProducts() {
        ArrayList<EventModel> cartList = new ArrayList<EventModel>();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    EventModel model = new EventModel();
                    model.setName(cursor.getString(cursor.getColumnIndex(COL_2)));
                    model.setDate(cursor.getString(cursor.getColumnIndex(COL_3)));
                    model.setTime(cursor.getString(cursor.getColumnIndex(COL_4)));
                    model.setDescription(cursor.getString(cursor.getColumnIndex(COL_5)));
                    model.setLocation(cursor.getString(cursor.getColumnIndex(COL_6)));
                    model.setPriority(cursor.getString(cursor.getColumnIndex(COL_7)));
                    cartList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return contact list
        return cartList;
    }

    public boolean ifBothExists(String id, String name) {
        Cursor cursor = null;
        String checkQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "= '" + id + "' AND " + COL_3 + "= '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

}
