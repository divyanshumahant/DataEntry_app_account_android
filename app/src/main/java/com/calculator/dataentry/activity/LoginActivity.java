package com.calculator.dataentry.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.ShowImageAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.PhotoModel;
import com.calculator.dataentry.model.SingleUserModel;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pd;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface ;
    EditText etName,etPassword;
    Button btnLogin;
    String name="",password="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);


        etName=(EditText)findViewById(R.id.etName);
        etPassword=(EditText)findViewById(R.id.etPasssword);
        btnLogin=(Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                name = etName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if(name.equals("")){
                    etName.setError("Please Enter Name");
                }else if(password.equals("")){
                    etPassword.setError("Please Enter password");
                }else{
                    Login(name,password);
                }
                break;

        }
    }


    public void Login(final String name, final String pas) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(LoginActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getlogin(name,pas);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                 pd.dismiss();
                    SingleUserModel responce = response.body();
                 if(responce.status.equals("success")){
                     sd.setUSER_STATUS("true");
                     sd.setUSER_ID(responce.userList.get(0).id);
                     sd.setTOKEN(responce.token);
                     Toast.makeText(LoginActivity.this,"Successfully Login",Toast.LENGTH_SHORT).show();
                     Intent in = new Intent(LoginActivity.this,StartActivity.class);
                     startActivity(in);
                 }else if(responce.status.equals("incorrect_password"))
                     Toast.makeText(LoginActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                 }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(LoginActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

}
