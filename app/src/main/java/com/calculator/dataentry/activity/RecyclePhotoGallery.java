package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.ReGalleryAdapter;
import com.calculator.dataentry.adapter.RephotoAdapter;
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

public class RecyclePhotoGallery extends AppCompatActivity {

    ProgressDialog pd ;
    ConnectionDetector cd;
    SessionManagment sd;
    APIInterface apiInterface;
    GridView gridView;
    private ArrayList<SingleUserModel.userDetails> listData;
    RephotoAdapter adapter;
    String id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery Recyclebin");

        pd = new ProgressDialog(RecyclePhotoGallery.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        gridView=(GridView) findViewById(R.id.gridview);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        listData=new ArrayList<>();

        Intent in = getIntent();
        id =  in.getStringExtra("id");

        getgallery(sd.getTOKEN(),id,"1");
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

    public void getgallery(String token,String uid,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(RecyclePhotoGallery.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getImage(token,uid,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        if(resource.status.equals("succes")){
                            listData.addAll(resource.userList);
                            adapter = new RephotoAdapter(RecyclePhotoGallery.this,listData,resource.imageUrl);
                            gridView.setAdapter(adapter);
                        }else if(resource.status.equals("noData")){
                            Toast.makeText(RecyclePhotoGallery.this, "No Data Exists", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(RecyclePhotoGallery.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }



    public void RestoreData(String token,String id, String img,final int position,String sta,String uid) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(RecyclePhotoGallery.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletImage(token,id,img,sta,uid);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("imageRestore")){
                        notifyAda(position);
                        Toast.makeText(RecyclePhotoGallery.this, "Image Successfully Restore", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RecyclePhotoGallery.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(RecyclePhotoGallery.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public void deleteData(String token,String id, String imag,final int position,String sta,String uid) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(RecyclePhotoGallery.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletImage(token,id,imag,sta,uid);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("imageDelete")){
                        notifyAda(position);
                        Toast.makeText(RecyclePhotoGallery.this, "permanent Delete Image", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(RecyclePhotoGallery.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(RecyclePhotoGallery.this, "please try again... !", Toast.LENGTH_SHORT).show();
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
