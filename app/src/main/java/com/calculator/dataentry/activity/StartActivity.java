package com.calculator.dataentry.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.calculator.dataentry.BuildConfig;
import com.calculator.dataentry.R;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.SingleUserModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class StartActivity extends AppCompatActivity  {
    private TextView txtCalender,txtLedger,tvRemainder;
    private LinearLayout llCalculator, llLedger,llPhoto,llRecycle;
    private SessionManagment sd;
    ProgressDialog pd;
    APIInterface apiInterface;
    ConnectionDetector cd;
    Dialog dialog ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        sd=new SessionManagment(StartActivity.this);
        llCalculator=(LinearLayout)findViewById(R.id.calender);
        llLedger=(LinearLayout)findViewById(R.id.ledger);
        llPhoto=(LinearLayout)findViewById(R.id.photo);
        llRecycle=(LinearLayout)findViewById(R.id.llrecycle);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        cd = new ConnectionDetector(this);
        dialog = new Dialog(StartActivity.this);

        permission();

        llLedger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StartActivity.this, DataListActivity.class);
                startActivity(i);
            }
        });

        llCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StartActivity.this, CalenderActivity.class);
                startActivity(i);
            }
        });

        llPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StartActivity.this, userImageActivity.class);
                startActivity(i);
            }
        });

        llRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StartActivity.this, RecycleActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.password,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.change:
                changePAssword();
                break;
                default:
                break;
            case R.id.changelogin:
                changePAssword1();
                break;
            case R.id.Logout:
                new android.app.AlertDialog.Builder(StartActivity.this)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to signout?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sd.setLOGIN_STATUS("false");
                                        sd.setUSER_STATUS("false");
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                        startActivity(i);
                                        finish();
                                        int pid = android.os.Process.myPid();
                                        android.os.Process.killProcess(pid);
                                        System.exit(0);
                                        finish();
                                    }

                                }).setNegativeButton("No", null).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changePAssword(){
        final Dialog dialog = new Dialog(StartActivity.this);
        dialog.setContentView(R.layout.changepassword);

        dialog.setTitle("Set Remainer...");
        dialog.show();
        final EditText edtOldPassword=(EditText)dialog.findViewById(R.id.edtOldPassword);
        final TextView edtNewPassword=(TextView)dialog.findViewById(R.id.edtNewPassword);
        final EditText confirmPassword=(EditText)dialog.findViewById(R.id.confirmPassword);
        Button btnUpdatePassword = (Button)dialog.findViewById(R.id.btnUpdatePassword);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

            String strOldPassword =edtOldPassword.getText().toString().trim();
            String strNewPassword =edtNewPassword.getText().toString().trim();
            String strConfirmPassword =confirmPassword.getText().toString().trim();
            if (strOldPassword.isEmpty()){
                edtOldPassword.setError("Please Enter Old Password");
            } else
            if (!strOldPassword.equalsIgnoreCase(sd.getPassword())){
                edtOldPassword.setError("Old Password Is Incorrect");

            }else   if (strNewPassword.isEmpty()){
                edtNewPassword.setError("Please Enter New Password");

            }else   if (strNewPassword.length()<6){
                edtNewPassword.setError("Minimum 6 digit");

            }else   if (strConfirmPassword.isEmpty()){
                confirmPassword.setError("Please Enter Confirm Password");

            }else  if (!strNewPassword.equalsIgnoreCase(strConfirmPassword)){
                confirmPassword.setError("Password Not Matched");

            }else  {
                sd.setPassword(strNewPassword);
                Toast.makeText(StartActivity.this,"Password Change Successfully",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Intent i=new Intent(StartActivity.this, MainActivity.class);
                startActivity(i);
                finish();
              }
            }
        });

    }

    @Override
    public void onBackPressed() {
        sd.setLOGIN_STATUS("false");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
    }

    public void permission() {
        if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_CALENDAR,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CALENDAR},
                        3);
            } else {
            }
        }
    }

    public void changepassword(String token,final String id, final String pas) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(StartActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getchangePassword(token,id,pas);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    pd.dismiss();
                    SingleUserModel responce = response.body();
                    if(responce.status.equals("succes")){
                        dialog.dismiss();
                        Toast.makeText(StartActivity.this,"Successfully Changed",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(StartActivity.this, "please try again...!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public void changePAssword1() {
        dialog.setContentView(R.layout.changepassword);
        dialog.setTitle("Change password...");
        dialog.show();
        final EditText edtOldPassword=(EditText)dialog.findViewById(R.id.edtOldPassword);
        final TextView edtNewPassword=(TextView)dialog.findViewById(R.id.edtNewPassword);
        final EditText confirmPassword=(EditText)dialog.findViewById(R.id.confirmPassword);
        Button btnUpdatePassword = (Button)dialog.findViewById(R.id.btnUpdatePassword);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                String strOldPassword =edtOldPassword.getText().toString().trim();
                String strNewPassword =edtNewPassword.getText().toString().trim();
                String strConfirmPassword =confirmPassword.getText().toString().trim();
                if (strOldPassword.isEmpty()){
                    edtOldPassword.setError("Please Enter Old Password");
                } else if (strNewPassword.isEmpty()){
                    edtNewPassword.setError("Please Enter New Password");
                }else   if (strNewPassword.length()<6){
                    edtNewPassword.setError("Minimum 6 digit");

                }else   if (strConfirmPassword.isEmpty()){
                    confirmPassword.setError("Please Enter Confirm Password");

                }else  if (!strNewPassword.equalsIgnoreCase(strConfirmPassword)){
                    confirmPassword.setError("Password Not Matched");

                }else  {
                    changepassword(sd.getTOKEN(),sd.getUSER_ID(),strConfirmPassword);
                }
            }
        });

    }



}
