package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.DataListAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.UserModel;
import com.calculator.dataentry.model.formDataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class DataListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataListAdapter adapter;
    private FloatingActionButton floating_action_button;
    private APIInterface apiInterface;
    private ConnectionDetector cd;
    private ProgressDialog pd;
    private TextView txtNoData;
    private Button btnSendData;
    private EditText edtName,edtMobile,edtEmail;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private final String Type="ledger";
    private ArrayList<UserModel.userDetails>listData;
    SessionManagment sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalist);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        listData=new ArrayList();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(DataListActivity.this);
        pd.setMessage("Please Wait..");
        pd.setCancelable(false);
        cd = new ConnectionDetector(DataListActivity.this);
        sd = new SessionManagment(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User List");

        floating_action_button = (FloatingActionButton) findViewById(R.id.floating_action_button);

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent i=new Intent(DataListActivity.this, FormActivity.class);
                startActivity(i);*/
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                edtName = (EditText) alertLayout.findViewById(R.id.edtName);
                edtMobile = (EditText) alertLayout.findViewById(R.id.edtMobile);
                edtEmail = (EditText) alertLayout.findViewById(R.id.edtEmail);
                btnSendData = (Button) alertLayout.findViewById(R.id.btnSendData);
                btnSendData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strName = edtName.getText().toString().trim();
                        String strEmail = edtEmail.getText().toString().trim();
                        String strMobile = edtMobile.getText().toString().trim();
                        if (strName.isEmpty()) {
                            edtName.setError("Please Enter Name");
                        } else {
                            if (strEmail.isEmpty()) {
                                strEmail = "";
                            }
                            if (strMobile.isEmpty()) {
                                strMobile = "";

                            }
                            sendData(sd.getTOKEN(),strName, strMobile, strEmail,Type);
                        }
                    }
                });
                alert = new AlertDialog.Builder(DataListActivity.this);
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                //alert.setCancelable(false);
                dialog = alert.create();
                dialog.show();
            }
        });
        getData(sd.getTOKEN(),Type,"0");

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(DataListActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(DataListActivity.this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                return false;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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


    public void sendData(String token,String name, String mobile, String email, String type) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(DataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.register(token,name,mobile,email,Type);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("success")){
                        if (dialog!=null)
                            dialog.dismiss();
                       getData(sd.getTOKEN(),Type,"0");

                    }else {
                        Toast.makeText(DataListActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    call.cancel();
                    Toast.makeText(DataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    private void notifyAda(int pos){
        listData.remove(pos);
        adapter.notifyDataSetChanged();

    }

    public void deleteData(String token,String id, final int position,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(DataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.registerDelete(token,id,sta);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("userRecyclerBin")){
                        if (dialog!=null)
                            dialog.dismiss();
                        notifyAda(position);
                        Toast.makeText(DataListActivity.this, "Move to recyclebin suceessfully", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(DataListActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    call.cancel();
                    Toast.makeText(DataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public void updateData(String token,String id,String name, String mobile, String email,final int position) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(DataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.registerUpdate(token,id,name,mobile,email);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("succes")){
                        if (dialog!=null)
                            dialog.dismiss();
                     /*   UserModel.userDetails =new UserModel.userDetails();
                        listData.add(position,);
                        adapter.notifyDataSetChanged();*/
                     getData(sd.getTOKEN(),Type,"0");

                        Toast.makeText(DataListActivity.this, "Update Details", Toast.LENGTH_SHORT).show();


                    }else {
                        Toast.makeText(DataListActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    call.cancel();
                    Toast.makeText(DataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }


    public void getData(String token,String type,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(DataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<UserModel> call = apiInterface.getAllUserData(token,type,sta);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, retrofit2.Response<UserModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("success")){
                        listData.clear();
                        txtNoData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        listData.addAll(response.body().userList);
                        adapter=new DataListAdapter(DataListActivity.this,listData);
                        recyclerView.setAdapter(adapter);
                    }else if (response.body().status.equalsIgnoreCase("NoData")){

                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);                    }
                }
                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    call.cancel();
                    Toast.makeText(DataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }
}
