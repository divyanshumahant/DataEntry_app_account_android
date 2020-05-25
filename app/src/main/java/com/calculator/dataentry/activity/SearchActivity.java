package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.Interface.DeletInterface;
import com.calculator.dataentry.Interface.SetPriorityInterface;
import com.calculator.dataentry.Interface.UpdateEventInterface;
import com.calculator.dataentry.Interface.UpdateInterface;
import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.EventAdapter;
import com.calculator.dataentry.adapter.SearchAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.AddEventModel;
import com.calculator.dataentry.model.SingleUserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchActivity extends AppCompatActivity  implements  DeletInterface, SetPriorityInterface {

     ConnectionDetector cd;
     SessionManagment sd;
     ProgressDialog pd;
     ImageView imgSearch;
     EditText etSeatch ;
     APIInterface apiInterface ;
     String search="";
     ListView listView;
     SearchAdapter adapter;
     ArrayList<AddEventModel> eventList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait....");
        imgSearch = (ImageView)findViewById(R.id.imgSearch);
        etSeatch=(EditText)findViewById(R.id.etSearch);
        listView = (ListView)findViewById(R.id.EventList);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        eventList=new ArrayList<>();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search View");

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = etSeatch.getText().toString().trim();
                getEventdata(sd.getTOKEN(),search);
            }
        });

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

    public void getEventdata(String token,String status) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SearchActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.SearchEvent(token,status);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        eventList.clear();
                        if(resource.status.equals("succes")){
                            for(int i=0;  i<resource.userList.size();  i++){
                                AddEventModel add = new AddEventModel();
                                add.setName(resource.userList.get(i).event_title);
                                add.setDate(resource.userList.get(i).event_date);
                                add.setStatus(resource.userList.get(i).event_status);
                                add.setId(resource.userList.get(i).id);
                                eventList.add(add);
                            }

                            adapter = new SearchAdapter(SearchActivity.this,eventList);
                            listView.setAdapter(adapter);
                        }else if(resource.status.equals("nodata")){
                            Toast.makeText(SearchActivity.this, "No Event according to your search", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(SearchActivity.this, "please try again...!", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(SearchActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(SearchActivity.this, "please try again...!", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }




    @Override
    public void DeletEvent(int position) {
        eventList.remove(position);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setPriorityEvent(String tok,String a, String b, String c, String d, String e, int p, String type) {
        updateEvent(tok,a,b,c,d,e,p, type);
    }

    public void updateEvent(String token,final String tit, final String sta, final String dat,final String id, final String sdate, final int pos,final String type) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SearchActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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
                        if(resource.status.equals("succes")){
                            add.setName(tit);
                            add.setDate(dat);
                            add.setStatus(sta);
                            add.setId(id);
                            eventList.set(pos,add);
                            adapter = new SearchAdapter(SearchActivity.this, eventList);
                            listView.setAdapter(adapter);

                            Toast.makeText(SearchActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SearchActivity.this, "Sorry, Try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(SearchActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


}
