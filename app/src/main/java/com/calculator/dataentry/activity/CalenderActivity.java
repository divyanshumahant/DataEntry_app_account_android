package com.calculator.dataentry.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.calculator.dataentry.BuildConfig;
import com.calculator.dataentry.Database.DatabaseHelper;
import com.calculator.dataentry.R;
import com.calculator.dataentry.model.EveryDay;
import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.desai.vatsal.mydynamiccalendar.OnEventClickListener;
import com.desai.vatsal.mydynamiccalendar.OnWeekDayViewClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalenderActivity extends AppCompatActivity  {

    private MyDynamicCalendar myCalendar;
    String title="",date="",time="",description="",location="",eventdate="",eventtime="",rdtae="";
    public static DatabaseHelper myDb;
    int years,months,days,hours,mins,seconds;
    public Uri eventUri;
    String currentDate="",showdate="";
    String m1 = "",m2="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_activity);

        myCalendar = (MyDynamicCalendar) findViewById(R.id.myCalendar);
        eventUri = Uri.parse("content://com.android.calendar/events");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calender View");
        myCalendar.showMonthView();

        myDb = new DatabaseHelper(CalenderActivity.this);


        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(Date date) {
                Intent in = new Intent(CalenderActivity.this,EventListActivity.class);
                String main = String.valueOf(date);
                String d = main.substring(8,10);
                String m = String.valueOf(date.getMonth()+1);
                String y = main.substring(30,34);
                if(m.length() == 1){
                    m1 = "0"+m;
                }else{
                    m1 = m;
                }
                currentDate = y+"-"+m1+"-"+d;
                showdate = d+"-"+m1+"-"+y;
                in.putExtra("sdate",showdate);
                startActivity(in);

                Log.e("date", String.valueOf(date));
            }
            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

        myCalendar.setHeaderBackgroundColor("#eb0976");
        myCalendar.setHeaderTextColor("#ffffff");
        myCalendar.setNextPreviousIndicatorColor("#ffffff");
        myCalendar.setWeekDayLayoutBackgroundColor("#ffffff");
        myCalendar.setWeekDayLayoutTextColor("#eb0976");
        myCalendar.setExtraDatesOfMonthBackgroundColor("#eb0976");
        myCalendar.setExtraDatesOfMonthTextColor("#ffffff");
        myCalendar.setDatesOfMonthBackgroundColor("#ffffff");
        myCalendar.setDatesOfMonthTextColor("#eb0976");
        myCalendar.setCurrentDateTextColor(R.color.black);
        myCalendar.setEventCellBackgroundColor("#852365");
        myCalendar.setEventCellTextColor("#425684");
        myCalendar.setBelowMonthEventTextColor("#425684");
        myCalendar.setBelowMonthEventDividerColor("#eb0976");
        myCalendar.setHolidayCellBackgroundColor("#eb0976");
        myCalendar.setHolidayCellTextColor("#d590bb");

        myCalendar.setHolidayCellClickable(false);
        myCalendar.addHoliday("2-11-2016");
        myCalendar.addHoliday("8-11-2016");
        myCalendar.addHoliday("12-11-2016");
        myCalendar.addHoliday("13-11-2016");
        myCalendar.addHoliday("8-10-2016");
        myCalendar.addHoliday("10-12-2016");
        myCalendar.addHoliday("10-12-2016");

        myCalendar.getEventList(new GetEventListListener() {
            @Override
            public void eventList(ArrayList<EventModel> eventList) {
                Log.e("tag", "eventList.size():-" + eventList.size());
                for (int i = 0; i < eventList.size(); i++) {
                    Log.e("tag", "eventList.getStrName:-" + eventList.get(i).getStrName());
                }
            }
        });
    }

    public void showdialog(){
        final Dialog dialog = new Dialog(CalenderActivity.this);
        dialog.setContentView(R.layout.custom_layout);
        dialog.setTitle("Set Remainer...");

        final EditText etTitle=(EditText)dialog.findViewById(R.id.etTitle);
        final TextView etTime=(TextView)dialog.findViewById(R.id.etTime);
        final TextView etDate=(TextView)dialog.findViewById(R.id.etDate);
        final EditText etDes=(EditText)dialog.findViewById(R.id.etDescription);
        final EditText etLoc=(EditText)dialog.findViewById(R.id.etLocation);
        Button btnSubmit = (Button)dialog.findViewById(R.id.btnSubmit);
        final Calendar cal;
        final DatePickerDialog.OnDateSetListener dat;
        cal = Calendar.getInstance();

        dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String  strDob = String.valueOf(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime()));
                if (strDob.equals(BuildConfig.FLAVOR)) {
                    strDob = BuildConfig.FLAVOR;
                }
                etDate.setText(strDob);
                eventdate = strDob;
                }
        };

      etDate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
                     new DatePickerDialog(CalenderActivity.this, dat,
                      cal.get(Calendar.YEAR),
                      cal.get(Calendar.MONTH),
                      cal.get(Calendar.DAY_OF_MONTH)).show();
          }
      });


      etTime.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Calendar mcurrentTime = Calendar.getInstance();
              int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
              int minute = mcurrentTime.get(Calendar.MINUTE);
              TimePickerDialog mTimePicker;
              mTimePicker = new TimePickerDialog(CalenderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                  @Override
                  public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                      etTime.setText(selectedHour + ":" + selectedMinute);
                      eventtime =selectedHour + ":" + selectedMinute;
                  }
              }, hour, minute,false);
              mTimePicker.setTitle("Select Time");
              mTimePicker.show();
          }
      });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                title = etTitle.getText().toString();
                rdtae = etDate.getText().toString();
                time=etTime.getText().toString();
                description=etDes.getText().toString();
                location=etLoc.getText().toString();

                if(title.equals("")){
                    Toast.makeText(CalenderActivity.this, "please Enter Event Name", Toast.LENGTH_SHORT).show();
                }else if(rdtae.equals("")){
                    Toast.makeText(CalenderActivity.this, "please Enter Event Date", Toast.LENGTH_SHORT).show();
                }else if(time.equals("")){
                    Toast.makeText(CalenderActivity.this, "please Enter Event Time", Toast.LENGTH_SHORT).show();
                }else if(description.equals("")){
                    Toast.makeText(CalenderActivity.this, "please Enter Event description", Toast.LENGTH_SHORT).show();
                }else if(location.equals("")){
                    Toast.makeText(CalenderActivity.this, "please Enter Event location", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    new android.app.AlertDialog.Builder(CalenderActivity.this)
                            .setTitle("Set Remainder.....")
                            .setMessage("Are you sure you want to create this event")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            myDb.insertEvent(title,rdtae,time,description,location,"");
                                            Toast.makeText(CalenderActivity.this, "Added Event Successfully", Toast.LENGTH_SHORT).show();
                                            onAddEventClicked(title,description,location);
                                        }
                                    }).setNegativeButton("No", null).show();

                }

            }
        });

        dialog.show();

    }

    public List<String> getDateAndTime(String date,String time) {
        List<String> list = new ArrayList<String>();
        String[] dateStr = date.split("-");
        String[] timeStr = time.split(":");
        for (String str : dateStr)
            list.add(str);

        for (String str : timeStr)
            list.add(str);

        return list;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAddEventClicked(String name,String description,String loc) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");


        List <String> dateTime = new ArrayList<String>();
        dateTime = getDateAndTime(eventdate,eventtime);

        years = Integer.parseInt(dateTime.get(0));
        months = Integer.parseInt(dateTime.get(1));
        days = Integer.parseInt(dateTime.get(2));
        hours = Integer.parseInt(dateTime.get(3));
        mins = Integer.parseInt(dateTime.get(4));
       // seconds = Integer.parseInt(dateTime.get(5));

        Calendar startTime = Calendar.getInstance();
        startTime.set(years, months-1, days, hours, mins,0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(years, months-1, days, hours, mins+30,0);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, name);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 2);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "My Guest House");
        values.put(CalendarContract.Events.EVENT_LOCATION, loc);
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        cr.insert(CalendarContract.Events.CONTENT_URI, values);

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        intent.putExtra(CalendarContract.Events.TITLE, name);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, loc);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        if(String.valueOf(month).length() == 1){
            m2 = "0"+String.valueOf(month);
        }else{
            m2 = String.valueOf(month);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_month:
                showMonthView();
                return true;
            case R.id.action_month_with_below_events:
                Intent in = new Intent(CalenderActivity.this, EventListActivity.class);
                String da = String.valueOf(date)+"-"+m2+"-"+String.valueOf(year);
                in.putExtra("sdate",da);
                startActivity(in);
                return true;
            case R.id.action_week:
                myCalendar.setCalendarDate(date,month,year);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showMonthView() {
        myCalendar.showMonthView();
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }





}