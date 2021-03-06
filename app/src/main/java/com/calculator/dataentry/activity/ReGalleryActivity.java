package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.ReGalleryAdapter;
import com.calculator.dataentry.adapter.ReLedgerAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.UserModel;
import com.calculator.dataentry.model.formDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ReGalleryActivity extends AppCompatActivity {

    ProgressDialog pd ;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface;
    ListView listView;
    private ArrayList<UserModel.userDetails> listData;
    ReGalleryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery Recyclebin");

        pd = new ProgressDialog(ReGalleryActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        listView=(ListView)findViewById(R.id.list);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        listData=new ArrayList<>();

        getgallery(sd.getTOKEN(),"1");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void getgallery(String token,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ReGalleryActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<UserModel> call = apiInterface.getGallery(token,sta);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, retrofit2.Response<UserModel> response) {
                    try {
                        pd.dismiss();
                        UserModel resource = response.body();
                        if(resource.status.equals("success")){
                            listData.addAll(resource.userList);
                            adapter = new ReGalleryAdapter(ReGalleryActivity.this,listData);
                            listView.setAdapter(adapter);
                        }else{
                            Toast.makeText(ReGalleryActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ReGalleryActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }



    public void RestoreData(String token,String id, final int position,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ReGalleryActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.DeletGallery(token,id,sta);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("userRestore")){
                        notifyAda(position);
                        Toast.makeText(ReGalleryActivity.this, "User Successfully Restore", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ReGalleryActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ReGalleryActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public void deleteData(String token,String id, final int position,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ReGalleryActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.DeletGallery(token,id,sta);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("userDelete")){
                        notifyAda(position);
                        Toast.makeText(ReGalleryActivity.this, "permanent Delete User", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ReGalleryActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ReGalleryActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }



    private void notifyAda(int pos){
        listData.remove(pos);
        adapter.notifyDataSetChanged();

    }


}
