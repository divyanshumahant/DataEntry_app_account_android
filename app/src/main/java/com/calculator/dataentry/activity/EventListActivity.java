package com.calculator.dataentry.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.calculator.dataentry.BuildConfig;
import com.calculator.dataentry.Interface.DeletInterface;
import com.calculator.dataentry.Interface.SetPriorityInterface;
import com.calculator.dataentry.Interface.UpdateEventInterface;
import com.calculator.dataentry.Interface.UpdateInterface;
import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.EventAdapter;
import com.calculator.dataentry.adapter.ShowImageAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.AddEventModel;
import com.calculator.dataentry.model.SingleUserModel;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class EventListActivity extends CalenderActivity
        implements  UpdateInterface, UpdateEventInterface, DeletInterface , SetPriorityInterface {

    ListView listView ;
    EventAdapter eventAdapter;
    TextView tvDate ;
    ArrayList<AddEventModel> eventList = null;
    LinearLayout llMain;
    String title="",sdate="",time="",description="",location="",eventdate="",eventtime="",rdtae="";
    private Toolbar toolbar;
    ConnectionDetector cd ;
    APIInterface apiInterface;
    ProgressDialog pd ;
    Calendar cal;
    DatePickerDialog.OnDateSetListener dat;
    String s1="",s2="",s3="",sfinal="";
    SessionManagment sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventlist_layout);

        listView= (ListView)findViewById(R.id.EventList);
        tvDate=(TextView)findViewById(R.id.tvDate);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        eventList = new ArrayList<>();
        sd = new SessionManagment(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Month's Event");

        cal = Calendar.getInstance();
        cd = new ConnectionDetector(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait....");

        Intent in = getIntent();
        sdate=in.getStringExtra("sdate");
        tvDate.setText(sdate);

        dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String  strDob = String.valueOf(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(cal.getTime()));
                if (strDob.equals(BuildConfig.FLAVOR)) {
                    strDob = BuildConfig.FLAVOR;
                }
                tvDate.setText(strDob);
                eventdate = strDob;

                finaldateEvent(tvDate.getText().toString());
            }
        };


       tvDate.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                           new DatePickerDialog(EventListActivity.this, dat,
                             cal.get(Calendar.YEAR),
                             cal.get(Calendar.MONTH),
                             cal.get(Calendar.DAY_OF_MONTH)).show();
          }
      });

       try {
           finaldateEvent(tvDate.getText().toString());
       }catch (Exception e){
           Toast.makeText(EventListActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
       }
    }

    public void finaldateEvent(String dat){
        s1 = dat.substring(0,2);
        s2 = dat.substring(3,5);
        s3 = dat.substring(6,10);
        sfinal=s3+"-"+s2+"-"+s1;
        getEventdata(sd.getTOKEN(),sfinal,"0");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_month:
                 showdialog();
                return true;
            case R.id.action_search:
                     Intent in = new Intent(EventListActivity.this,SearchActivity.class);
                     startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void showdialog(){
        final Dialog dialog = new Dialog(EventListActivity.this);
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

        etTime.setVisibility(View.GONE);
        etDes.setVisibility(View.GONE);
        etLoc.setVisibility(View.GONE);
        btnSubmit.setText("Set Event");

        dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String  strDob = String.valueOf(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(cal.getTime()));
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
                new DatePickerDialog(EventListActivity.this, dat,
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
                mTimePicker = new TimePickerDialog(EventListActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                    Toast.makeText(EventListActivity.this, "please Enter Event Name", Toast.LENGTH_SHORT).show();
                }else if(rdtae.equals("")){
                    Toast.makeText(EventListActivity.this, "please Enter Event Date", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    s1 = rdtae.substring(0,2);
                    s2 = rdtae.substring(3,5);
                    s3 = rdtae.substring(6,10);
                    sfinal=s3+"-"+s2+"-"+s1;

                    new android.app.AlertDialog.Builder(EventListActivity.this)
                            .setTitle("Set Remainder.....")
                            .setMessage("Are you sure you want to create this event")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            insertdata(sd.getTOKEN(),title,sfinal,rdtae);
                                        }
                                    }).setNegativeButton("No", null).show();
                }

            }
        });

        dialog.show();

    }

    public void getEventdata(String token,String date,String status) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(EventListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getEventList(token,date,status);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        eventList.clear();
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        if(resource.status.equals("succes")){
                            llMain.setVisibility(View.VISIBLE);

                            for(int i=0;  i<resource.userList.size();  i++){
                                AddEventModel add = new AddEventModel();
                                add.setName(resource.userList.get(i).event_title);
                                add.setDate(resource.userList.get(i).event_date);
                                add.setStatus(resource.userList.get(i).event_status);
                                add.setId(resource.userList.get(i).id);
                                eventList.add(add);
                            }
                            eventAdapter = new EventAdapter(EventListActivity.this,eventList);
                            listView.setAdapter(eventAdapter);
                        }else if(resource.status.equals("nodata")){
                            llMain.setVisibility(View.GONE);
                            Toast.makeText(EventListActivity.this, "No Events Exists", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){

                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(EventListActivity.this, "please try again...!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    public void insertdata(String token,final String title, final String date, final String rdate) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(EventListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getInsert_Event(token,title,date);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                   try {
                       pd.dismiss();
                       SingleUserModel resource = response.body();
                       if(resource.status.equals("succes")){
                           AddEventModel add = new AddEventModel();
                           if(tvDate.getText().toString().equals(rdate)){
                               JsonElement id=resource.data.get("insertId");
                               JsonElement status=resource.data.get("serverStatus");
                               add.setId(id.getAsString());
                               add.setStatus(status.getAsString());
                               add.setDate(date);
                               add.setName(title);

                               eventList.add(0,add);
                               eventAdapter = new EventAdapter(EventListActivity.this, eventList);
                               listView.setAdapter(eventAdapter);
                               eventAdapter.notifyDataSetChanged();
                               listView.smoothScrollToPosition(0);
                               Toast.makeText(EventListActivity.this, "Create event successfully", Toast.LENGTH_SHORT).show();

                           }else{
                               String d1 = date.substring(0,4);
                               String s2 = date.substring(5,7);
                               String s3 = date.substring(8,10);
                               String fi= s3+"-"+s2+"-"+d1;
                               new android.app.AlertDialog.Builder(EventListActivity.this)
                                       .setTitle("Event")
                                       .setMessage("Your event is successfully created on "+fi+", You can Go and see there")
                                       .setPositiveButton("",
                                               new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                   }

                                               }).setNegativeButton("", null).show();

                           }
                       }else{
                           Toast.makeText(EventListActivity.this, "Sorry, Try after some time", Toast.LENGTH_SHORT).show();
                       }

                   }catch (Exception e){
                       Toast.makeText(EventListActivity.this, "Sorry Try after some time", Toast.LENGTH_SHORT).show();

                   }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(EventListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    @Override
    public void updatedate() {
        finaldateEvent(tvDate.getText().toString());
    }


    @Override
    public void UpdateEvent(final String det) {
        finaldateEvent(tvDate.getText().toString());
        if(!tvDate.getText().toString().equals(det)){
            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
                @Override
                public void run() {
                    new android.app.AlertDialog.Builder(EventListActivity.this)
                            .setTitle("Event")
                            .setMessage("Your event is successfully created on "+det+", You can Go and see there")
                            .setPositiveButton("",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                                    }).setNegativeButton("", null).show();

                }
            }, 2500);

        }
    }

    @Override
    public void DeletEvent(int position) {
        eventList.remove(position);
        eventAdapter.notifyDataSetChanged();
    }


    @Override
    public void setPriorityEvent(String tok,String a, String b, String c, String d, String e, int p, String type) {
       updateEvent(tok,a,b,c,d,e,p, type);
    }

    public void updateEvent(String token,final String tit, final String sta, final String dat,final String id, final String sdate, final int pos,final String type) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(EventListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getupdateEvent(token,tit,sta,dat,id);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        AddEventModel add = new AddEventModel();
                        SingleUserModel resource = response.body();
                        if(resource.status.equals("succes")&&sdate.equals(tvDate.getText().toString())){
                          add.setName(tit);
                          add.setDate(dat);
                          add.setStatus(sta);
                          add.setId(id);
                          eventList.set(pos,add);
                          eventAdapter = new EventAdapter(EventListActivity.this, eventList);
                          listView.setAdapter(eventAdapter);
                          Toast.makeText(EventListActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }else if(resource.status.equals("succes")&& !sdate.equals(tvDate.getText().toString())){
                            DeletEvent(pos);
                            new android.app.AlertDialog.Builder(EventListActivity.this)
                                    .setTitle("Event")
                                    .setMessage("Your event is successfully created on "+sdate+", You can Go and see there")
                                    .setPositiveButton("",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }

                                            }).setNegativeButton("", null).show();
                        }else{
                            Toast.makeText(EventListActivity.this, "Sorry, Try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(EventListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


}
