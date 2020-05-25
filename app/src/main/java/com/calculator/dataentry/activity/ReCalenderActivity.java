package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.ReLedgerAdapter;
import com.calculator.dataentry.adapter.RecalenderAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.SingleUserModel;
import com.calculator.dataentry.model.UserModel;
import com.calculator.dataentry.model.formDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ReCalenderActivity extends AppCompatActivity {

    ProgressDialog pd;
    ConnectionDetector cd;
    APIInterface apiInterface ;
    ListView listView;
    ArrayList<SingleUserModel.userDetails> al ;
    RecalenderAdapter adapter;
    SessionManagment sd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calender Recyclebin");

        pd = new ProgressDialog(ReCalenderActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        listView=(ListView)findViewById(R.id.list);
        al =new ArrayList<>();

        getCalender(sd.getTOKEN(),"1");

    }

    public void getCalender(String token,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ReCalenderActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getReCalender(token,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        if(resource.status.equals("success")){
                            al.addAll(resource.userList);
                            adapter = new RecalenderAdapter(ReCalenderActivity.this,al);
                            listView.setAdapter(adapter);
                        }else if(resource.status.equals("NoData")){
                            Toast.makeText(ReCalenderActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();
                        }else if(resource.status.equals("Invalid")){
                            Toast.makeText(ReCalenderActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ReCalenderActivity.this, "try after some time", Toast.LENGTH_SHORT).show();

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ReCalenderActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    public void deleteData(String token,String id, final int position,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ReCalenderActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletEvent(token,id,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("eventDelete")){
                        notifyAda(position);
                        Toast.makeText(ReCalenderActivity.this, "permanent Delete Event", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ReCalenderActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ReCalenderActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public void RestoreData(String token,String id, final int position,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(ReCalenderActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletEvent(token,id,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("eventRestore")){
                        notifyAda(position);
                        Toast.makeText(ReCalenderActivity.this, "Successfully Restore Event", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ReCalenderActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ReCalenderActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }



    private void notifyAda(int pos){
        al.remove(pos);
        adapter.notifyDataSetChanged();

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
}
