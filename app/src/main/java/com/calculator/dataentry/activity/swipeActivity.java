package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.calculator.dataentry.Interface.UpdateInterface;
import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.swipeAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.SingleUserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.desai.vatsal.mydynamiccalendar.AppConstants.eventList;

public class swipeActivity  extends AppCompatActivity implements UpdateInterface {

    APIInterface apiInterface;
    ProgressDialog pd ;
    ConnectionDetector cd;
    ViewPager viewPager;
    swipeAdapter swipeAdapter ;
    String id = "",image="";
    int pos =0;
    static ArrayList<SingleUserModel.userDetails> imagelist;
    SessionManagment sd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_layout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pd = new ProgressDialog(swipeActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait....");
        cd = new ConnectionDetector(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sd = new SessionManagment(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagelist=new ArrayList<>();
        getSupportActionBar().setTitle("Users Image");

        Intent in = getIntent();
        id =  in.getStringExtra("id");
        image =  in.getStringExtra("image");
        pos = Integer.parseInt(in.getStringExtra("pos"));

       getUserImage(sd.getTOKEN(),id,"0");

    }

    public void getUpdate(String token,final String id,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(swipeActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
          //  pd.show();
            Call<SingleUserModel> call = apiInterface.getImage(token,id,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                      //  pd.dismiss();
                        SingleUserModel resource = response.body();
                        imagelist =resource.userList;
                        if(resource.status.equals("succes")){
                            swipeAdapter = new swipeAdapter(swipeActivity.this,imagelist,resource.imageUrl);
                            swipeAdapter.notifyDataSetChanged();
                            viewPager.setAdapter(swipeAdapter);
                            viewPager.setCurrentItem(pos);
                        }else if(resource.status.equals("noData")){
                            Toast.makeText(swipeActivity.this, "No image exists", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(swipeActivity.this, PhotoGalleryActivity.class);
                            startActivity(in);
                            finish();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                      //  pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(swipeActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                  //  pd.dismiss();
                }
            });
        }

    }

    public void getUserImage(String token,String id,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(swipeActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getImage(token,id,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        imagelist.addAll(resource.userList);
                        if(resource.status.equals("succes")){
                            swipeAdapter = new swipeAdapter(swipeActivity.this,imagelist,resource.imageUrl);
                            swipeAdapter.notifyDataSetChanged();
                            viewPager.setAdapter(swipeAdapter);
                            viewPager.setCurrentItem(pos);
                        }else if(resource.status.equals("noData")){
                            Toast.makeText(swipeActivity.this, "No image exists", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(swipeActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
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


    public void getDeletImage(String token,String id,String image,String sta,final int posi,String uid) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(swipeActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletImage(token,id,image,sta,uid);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        if(resource.status.equals("imageRecyclerBin")){
                            Toast.makeText(swipeActivity.this, "Move to Recyclebin Successfully", Toast.LENGTH_SHORT).show();
                             imageDelet(posi);
                        }else{
                            Toast.makeText(swipeActivity.this, "Sorry, try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(swipeActivity.this, "please try again...!", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(swipeActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    public void imageDelet(int position) {
        imagelist.remove(position);
        swipeAdapter.notifyDataSetChanged();
    }



    @Override
    public void updatedate() {
        getUpdate(sd.getTOKEN(),id,"0");
    }
}