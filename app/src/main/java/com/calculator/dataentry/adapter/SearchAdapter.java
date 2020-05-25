package com.calculator.dataentry.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.calculator.dataentry.BuildConfig;
import com.calculator.dataentry.Database.DatabaseHelper;
import com.calculator.dataentry.Interface.DeletInterface;
import com.calculator.dataentry.Interface.SetPriorityInterface;
import com.calculator.dataentry.Interface.UpdateEventInterface;
import com.calculator.dataentry.Interface.UpdateInterface;
import com.calculator.dataentry.R;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.AddEventModel;
import com.calculator.dataentry.model.SingleUserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchAdapter  extends ArrayAdapter {

    ArrayList<AddEventModel> al = null;
    Context context;
    DatabaseHelper mydb;/*
    UpdateInterface updateInterface;
    UpdateEventInterface updateEventInterface;*/
    SetPriorityInterface setPriorityInterface ;
    DeletInterface deletInterface;
    ProgressDialog pd ;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface;
    String title="",date="",time="",description="",location="",
            eventdate="",eventtime="",rdtae="",sdate="",s1="",s2="",s3="";

    public SearchAdapter(Context context, ArrayList<AddEventModel> al) {
        super(context, R.layout.serach_items, al);
        this.context=context;
        this.al = al;
      /*  updateInterface=(UpdateInterface)context;
        updateEventInterface=(UpdateEventInterface)context;*/
        deletInterface = (DeletInterface)context;
        setPriorityInterface = (SetPriorityInterface)context;
        mydb = new DatabaseHelper(context);
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        sd= new SessionManagment(context);
        cd = new ConnectionDetector(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder vh = new ViewHolder();
        final int pos = position;
        final AddEventModel lm = al.get(pos);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.serach_items, parent, false);
            vh.tvTitle=(TextView)convertView.findViewById(R.id.tvName);
            vh.tvdate=(TextView)convertView.findViewById(R.id.tvdate);
            vh.tvTime=(TextView)convertView.findViewById(R.id.tvtime);
            vh.tvDescription=(TextView)convertView.findViewById(R.id.tvDescription);
            vh.tvDelet=(TextView)convertView.findViewById(R.id.tvDelete);
            vh.tvUpdate=(TextView)convertView.findViewById(R.id.tvUpdate);
            vh.tvLocation=(TextView)convertView.findViewById(R.id.tvlocation);
            vh.tvprioroty=(TextView)convertView.findViewById(R.id.tvPriority);
            vh.tvRemove=(TextView)convertView.findViewById(R.id.tvRemovePriority);
            vh.llContainer=(LinearLayout)convertView.findViewById(R.id.llContainer);
            vh.llMain=(LinearLayout)convertView.findViewById(R.id.llMain);
            convertView.setTag(vh);
        } else {
            vh =(ViewHolder)convertView.getTag();
        }

        vh.tvTitle.setText(lm.getName());
        date = lm.getDate().substring(0,10);
        s1 = date.substring(0,4);
        s2 = date.substring(5,7);
        s3 = date.substring(8,10);
        sdate=s3+"-"+s2+"-"+s1;
        vh.tvdate.setText(sdate);


        // vh.tvTime.setText(lm.getTime());
        // vh.tvDescription.setText(lm.getDescription());
        // vh.tvLocation.setText(lm.getLocation());
        final ViewHolder finalVh = vh;

        if(lm.getStatus().equals("1")){
            vh.tvprioroty.setVisibility(View.GONE);
            vh.tvRemove.setVisibility(View.VISIBLE);
            finalVh.llContainer.setBackgroundColor(Color.RED);
            finalVh.llMain.setBackgroundColor(new Color().parseColor("#48E8EE"));
        }else{
            vh.tvRemove.setVisibility(View.GONE);
            vh.tvprioroty.setVisibility(View.VISIBLE );
            finalVh.llContainer.setBackgroundColor(new Color().parseColor("#dddddd"));
            finalVh.llMain.setBackgroundColor(Color.WHITE);
        }

        final ViewHolder finalVh1 = vh;
        final ViewHolder finalVh2 = vh;

        vh.tvprioroty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Set Event to Priority.....")
                        .setMessage("Are you sure you want to set this event to priority")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finalVh.llContainer.setBackgroundColor(Color.RED);
                                        finalVh.llMain.setBackgroundColor(new Color().parseColor("#48E8EE"));
                                        setPriorityInterface.setPriorityEvent(sd.getTOKEN(),lm.getName(),"1",lm.getDate().substring(0,10),lm.getId(),sdate,pos,"pri");
                                        // updateEvent(lm.getName(),"1",lm.getDate().substring(0,10),lm.getId(),sdate);
                                        finalVh1.tvprioroty.setVisibility(View.GONE);
                                        finalVh2.tvRemove.setVisibility(View.VISIBLE);
                                        //  editInterface.editEvent(tit,title,description,startTime.getTimeInMillis(),endTime.getTimeInMillis());
                                        // myDb.updateData(tit,title,rdtae,time,description,location,"");
                                        //  Toast.makeText(context, "update Event Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("No", null).show();

            }
        });

        vh.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Set Event to Priority.....")
                        .setMessage("Are you sure you want to remove this event to priority")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finalVh.llContainer.setBackgroundColor(new Color().parseColor("#dddddd"));
                                        finalVh.llMain.setBackgroundColor(Color.WHITE);

                                        setPriorityInterface.setPriorityEvent(sd.getTOKEN(),lm.getName(),"0",lm.getDate().substring(0,10),lm.getId(),sdate,pos,"pri");
                                        // updateEvent(lm.getName(),"0",lm.getDate().substring(0,10),lm.getId(),sdate);
                                        finalVh1.tvprioroty.setVisibility(View.VISIBLE);
                                        finalVh2.tvRemove.setVisibility(View.GONE);
                                        //  editInterface.editEvent(tit,title,description,startTime.getTimeInMillis(),endTime.getTimeInMillis());
                                        // myDb.updateData(tit,title,rdtae,time,description,location,"");
                                        //  Toast.makeText(context, "update Event Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        vh.tvDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Delete Event.....")
                        .setMessage("Are you sure you want to delet this event")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            deletdata(sd.getTOKEN(),lm.getId(),pos,"1");
                                        }catch (Exception e){
                                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        vh.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog(lm.getName(),sdate,lm.getId(),pos);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle,tvdate,tvTime,tvDescription,tvDelet,tvUpdate,tvLocation,tvprioroty,tvRemove;
        LinearLayout llContainer ,llMain;
    }

    public void showdialog(final String tit, String det, final String id,final int posi){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_layout);
        dialog.setTitle("Set Event...");

        final EditText etTitle=(EditText)dialog.findViewById(R.id.etTitle);
        final TextView etTime=(TextView)dialog.findViewById(R.id.etTime);
        final TextView etDate=(TextView)dialog.findViewById(R.id.etDate);
        final EditText etDes=(EditText)dialog.findViewById(R.id.etDescription);
        final EditText etLoc=(EditText)dialog.findViewById(R.id.etLocation);
        Button btnSubmit = (Button)dialog.findViewById(R.id.btnSubmit);
        final Calendar cal;
        final DatePickerDialog.OnDateSetListener dat;
        cal = Calendar.getInstance();

        etDes.setVisibility(View.GONE);
        etLoc.setVisibility(View.GONE);
        etTime.setVisibility(View.GONE);
        btnSubmit.setText("Update");

        etTitle.setText(tit);
        etDate.setText(det.substring(0,10));


        dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
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
                new DatePickerDialog(context, dat,
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
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
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

                s1 = rdtae.substring(0,2);
                s2 = rdtae.substring(3,5);
                s3 = rdtae.substring(6,10);
                final String sfinal=s3+"-"+s2+"-"+s1;

                if(title.equals("")){
                    Toast.makeText(context, "please Enter Event Name", Toast.LENGTH_SHORT).show();
                }else if(rdtae.equals("")){
                    Toast.makeText(context, "please Enter Event Date", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    new android.app.AlertDialog.Builder(context)
                            .setTitle("Update Event.....")
                            .setMessage("Are you sure you want to update this event ?")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // updateEvent(title,"0",sfinal,id,rdtae);
                                            setPriorityInterface.setPriorityEvent(sd.getTOKEN(),title,"0",sfinal,id,rdtae,posi,"update");
                                            //  editInterface.editEvent(tit,title,description,startTime.getTimeInMillis(),endTime.getTimeInMillis());
                                            // myDb.updateData(tit,title,rdtae,time,description,location,"");
                                            //  Toast.makeText(context, "update Event Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton("No", null).show();

                }

            }
        });

        dialog.show();
    }


    public void deletdata(String token,String id,final int Positon,String status) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletEvent(token,id,status);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        final SingleUserModel resource = response.body();
                        if(resource.status.equals("eventRecyclerBin")){
                            Toast.makeText(context, "Move to Recyclebin Successfully", Toast.LENGTH_SHORT).show();
                            deletInterface.DeletEvent(Positon);
                        }else{
                            Toast.makeText(context, "Sorry, Try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(context, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }




}