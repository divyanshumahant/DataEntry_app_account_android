package com.calculator.dataentry.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.calculator.dataentry.BuildConfig;
import com.calculator.dataentry.R;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.HistoryModel;
import com.calculator.dataentry.model.SingleUserModel;
import com.calculator.dataentry.model.formDataModel;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class SingleDataListActivity extends AppCompatActivity implements View.OnClickListener {
    private APIInterface apiInterface;
    private ConnectionDetector cd;
    private ProgressDialog pd;
    private TextView txtNoData,textDate,txtTotalCredit,txtDebitTotal,grandTotal;
    private Button btnSendData,btnUpdateData,btnDeleteData;
    private EditText edtName,edtMobile,edtEmail,edtdebitAmount,edtCreditAmount;
    private AlertDialog.Builder alert,actionAlert;
    private AlertDialog dialog,actionDialog;
    private String userId,strName,strEmail,strMobile;
    private View alertLayout,actionAlertLayout;
    private List<HistoryModel> list;
    private double totalBalance;
    private double credit,debit;
    private DateFormat formatter = null;
    private Date convertedDate = null;
    SessionManagment sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_table);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd=new ProgressDialog(SingleDataListActivity.this);
        pd.setMessage("Please Wait..");
        sd = new SessionManagment(this);
        list=new ArrayList<>();
        pd.setCancelable(false);
        cd=new ConnectionDetector(SingleDataListActivity.this);
        userId=getIntent().getStringExtra("id");
        strName=getIntent().getStringExtra("name");
        strEmail=getIntent().getStringExtra("email");
        strMobile=getIntent().getStringExtra("mobile");
        getSupportActionBar().setTitle(strName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHeaders();

        getData(sd.getTOKEN(),userId);
 }


    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
      //  tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
      //  tv.setOnClickListener(this);
        return tv;
    }

    private EditText getEditText(int id, String title, int color, int typeface, int bgColor) {
        EditText tv = new EditText(this);
      //  tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
      //  tv.setOnClickListener(this);
        return tv;
    }
    private TextView getImageView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
     //   tv.setCompoundDrawables(android.R.drawable.actio);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {
       TableLayout  tl = findViewById(R.id.table);
        tl.removeAllViews();


        TableRow tr = new TableRow(this);

        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Date", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tr.addView(getTextView(0, "Particulars", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tr.addView(getTextView(0, "Debit Amount", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tr.addView(getTextView(0, "Credit Amount", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tr.addView(getTextView(0, "C/D", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tr.addView(getTextView(0, "Balance", Color.BLACK, Typeface.BOLD, Color.GRAY));
       // tr.addView(getTextView(0, "EDIT", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tr.addView(getTextView(0, "Action", Color.BLACK, Typeface.BOLD, Color.GRAY));
        tl.addView(tr, getTblLayoutParams());
    }



    public void sendData(String token,String finaldate,final String Id,String particular,String debitAmount,String creditAmount) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SingleDataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.insertHistory(token,finaldate,Id,particular,debitAmount,creditAmount);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    try{
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("success")){
                        if (dialog!=null)
                            dialog.dismiss();
                       getData(sd.getTOKEN(),Id);

                    }else if (response.body().status.equalsIgnoreCase("already")){
                        Toast.makeText(SingleDataListActivity.this, "Mobile Or Email Already Register", Toast.LENGTH_SHORT).show();

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(SingleDataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }

    public void deleteData(String token,final String deleteId) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SingleDataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.getDeleteDataHistory(token,deleteId);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("succes")){
                        if (dialog!=null)
                            dialog.dismiss();
                        Toast.makeText(SingleDataListActivity.this, "Remove From List", Toast.LENGTH_SHORT).show();

                        getData(sd.getTOKEN(),userId);

                    }else if (response.body().status.equalsIgnoreCase("already")){
                        Toast.makeText(SingleDataListActivity.this, "Mobile Or Email Already Register", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(SingleDataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }

    public void updateData(String token,String finaldate,final String particulars,final String debit_amount,final String credit_amount,final String updateId) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SingleDataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<formDataModel> call = apiInterface.getupdateHistory(token,finaldate,particulars,debit_amount,credit_amount,updateId);
            call.enqueue(new Callback<formDataModel>() {
                @Override
                public void onResponse(Call<formDataModel> call, retrofit2.Response<formDataModel> response) {
                    pd.dismiss();
                    if (response.body().status.equalsIgnoreCase("succes")){
                        if (dialog!=null)
                            dialog.dismiss();
                        Toast.makeText(SingleDataListActivity.this, "Data Update", Toast.LENGTH_SHORT).show();

                        getData(sd.getTOKEN(),userId);

                    }else if (response.body().status.equalsIgnoreCase("already")){
                       // Toast.makeText(SingleDataListActivity.this, "Mobile Or Email Already Register", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<formDataModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(SingleDataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }
    /**
     * This function add the data to the table
     **/
    public void addData(List<HistoryModel> list) {
        int numCompanies = list.size();
        TableLayout tl = findViewById(R.id.table);
        int pos=0;

        for (int i = 0; i < numCompanies; i++) {

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + numCompanies,list.get(i).getDate().trim() , Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.addView(getTextView(i + numCompanies, list.get(i).getPerticular().trim(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.addView(getTextView(i + numCompanies, list.get(i).getDebit_amount().trim(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.addView(getTextView(i + numCompanies, list.get(i).getCurrent_amount().trim(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.addView(getTextView(i + numCompanies, list.get(i).getStatus().trim(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.addView(getTextView(i + numCompanies, list.get(i).getBalance().trim(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
        //    tr.addView(getImageView(i + 1, "Edit", android.R.color.holo_green_dark, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.addView(getImageView(list.get(i).getPos() , "Action", Color.RED, Typeface.NORMAL, ContextCompat.getColor(this, R.color.backcolor)));
            tr.setOnClickListener(this);
            tl.addView(tr, getTblLayoutParams());
            pos++;
        }
    }

    public void getData(String token,String userId) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(SingleDataListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getSingleUserDetails(token,userId);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    pd.dismiss();
                    list.clear();
                    if (response.body().status.equalsIgnoreCase("success")){
                        addHeaders();
                        for (int i=0;i<response.body().userList.size();i++){
                            HistoryModel historyModel =new HistoryModel();
                            historyModel.setPos(i);
                            if(response.body().userList.get(i).debit_amount.isEmpty()){
                                debit =0.0;
                            }else {
                                debit= Double.parseDouble(response.body().userList.get(i).debit_amount);
                            }
                            if (response.body().userList.get(i).credit_amount.isEmpty()){
                                credit = 0.0;
                            }else {
                               credit= Double.parseDouble(response.body().userList.get(i).credit_amount);
                            }
                            historyModel.setDebit_amount(String.format("%.2f",debit));
                            historyModel.setCurrent_amount(String.format("%.2f",credit));
                            historyModel.setPerticular(response.body().userList.get(i).particulars);
                            historyModel.setId(response.body().userList.get(i).id);
                            historyModel.setDate(response.body().userList.get(i).finaldate);
                            if (list.size()==0){
                                totalBalance = totalFormula(credit,debit,0.0);
                            }else {
                                totalBalance = totalFormula(credit,debit, Double.parseDouble(list.get(i-1).getBalance()));

                            }
                            if ( totalBalance >= 0){
                                historyModel.setStatus("Dr.");
                            }else {
                            historyModel.setStatus("Cr.");
                            }
                            historyModel.setBalance(String.format("%.2f",totalBalance));
                            list.add(historyModel);

                        }

                        addData(list);

                    }else if (response.body().status.equalsIgnoreCase("NoData")){
                        addHeaders();
/*
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        cardview.setVisibility(View.GONE);*/
                    }
                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                  /*  txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    cardview.setVisibility(View.GONE);*/

                    call.cancel();
                    Toast.makeText(SingleDataListActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }
        private Double totalFormula(double creditAmount,double debitAmount,double balance){
        double total = (debitAmount - creditAmount)+balance;
        return total;
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add:
                LayoutInflater inflater = getLayoutInflater();
                alertLayout = inflater.inflate(R.layout.add_details, null);
                edtName = (EditText) alertLayout.findViewById(R.id.edtName);
                edtMobile =(EditText) alertLayout.findViewById(R.id.edtMobile);
                edtEmail = (EditText) alertLayout.findViewById(R.id.edtEmail);
                edtdebitAmount = (EditText) alertLayout.findViewById(R.id.debitAmount);
                edtCreditAmount = (EditText) alertLayout.findViewById(R.id.creditAmount);
              //  radioTransaction = (RadioGroup) alertLayout.findViewById(R.id.radioTransaction);
                textDate=(TextView) alertLayout.findViewById(R.id.textDate );

                btnSendData = (Button) alertLayout.findViewById(R.id.btnSendData);
                final Calendar cal;
                final DatePickerDialog.OnDateSetListener dat;
                cal = Calendar.getInstance();



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
                        textDate.setText(strDob);
                    }
                };

                textDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(SingleDataListActivity.this, dat,
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                btnSendData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strParticular= edtName.getText().toString().trim();
                        String strInvoice= edtEmail.getText().toString().trim();
                        String strAmountType= edtMobile.getText().toString().trim();
                        String strDate=textDate.getText().toString().trim();
                        String strDebitAmount=edtdebitAmount.getText().toString().trim();
                        String strCreditAmount=edtCreditAmount.getText().toString().trim();




                        if (strParticular.isEmpty()){
                            strParticular="";
                        }
                        if (strCreditAmount.isEmpty()){
                                strCreditAmount="0.0";
                        } if (strDebitAmount.isEmpty()){
                                strDebitAmount="0.0";
                        }if (strDate.isEmpty()){
                            strDate="";
                        }

                        sendData(sd.getTOKEN(),strDate,userId,strParticular,strDebitAmount,strCreditAmount);
                    }
                });
                alert = new AlertDialog.Builder(SingleDataListActivity.this);
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                //alert.setCancelable(false);

                dialog = alert.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        TextView et = findViewById(id);
        if (et!=null){
            LayoutInflater actionInflater = getLayoutInflater();
            actionAlertLayout = actionInflater.inflate(R.layout.update_details, null);
            btnUpdateData = (Button) actionAlertLayout.findViewById(R.id.btnUpdateData);
            btnDeleteData =(Button) actionAlertLayout.findViewById(R.id.btnDeleteData);
            actionAlert = new AlertDialog.Builder(SingleDataListActivity.this);
            btnUpdateData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionDialog.dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    alertLayout = inflater.inflate(R.layout.add_details, null);
                    LinearLayout linearId = (LinearLayout) alertLayout.findViewById(R.id.linearId);
                   TextView txtDetails = (TextView) alertLayout.findViewById(R.id.txtDetails);
                    edtName = (EditText) alertLayout.findViewById(R.id.edtName);

                    txtDetails.setText("Update Details");
                    edtMobile =(EditText) alertLayout.findViewById(R.id.edtMobile);
                    edtEmail = (EditText) alertLayout.findViewById(R.id.edtEmail);
                    edtdebitAmount = (EditText) alertLayout.findViewById(R.id.debitAmount);
                    edtCreditAmount = (EditText) alertLayout.findViewById(R.id.creditAmount);
                    edtCreditAmount.setText(list.get(id).getCurrent_amount());
                    edtdebitAmount.setText(list.get(id).getDebit_amount());
                    edtName.setText(list.get(id).getPerticular());
                    //  radioTransaction = (RadioGroup) alertLayout.findViewById(R.id.radioTransaction);
                    textDate=(TextView) alertLayout.findViewById(R.id.textDate );
                    textDate.setText(list.get(id).getDate());
                    final Calendar cal;
                    final DatePickerDialog.OnDateSetListener dat;
                    cal = Calendar.getInstance();



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
                            textDate.setText(strDob);
                        }
                    };


                    textDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(SingleDataListActivity.this, dat,
                                    cal.get(Calendar.YEAR),
                                    cal.get(Calendar.MONTH),
                                    cal.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    btnSendData = (Button) alertLayout.findViewById(R.id.btnSendData);
                    btnSendData.setText("Update");
                    btnSendData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String strDebitAmount=edtdebitAmount.getText().toString().trim();
                            String strCreditAmount=edtCreditAmount.getText().toString().trim();
                            String strParticular= edtName.getText().toString().trim();
                            String strDate=textDate.getText().toString().trim();



                            if (strParticular.isEmpty()){
                                strParticular="";
                            }
                            if (strCreditAmount.isEmpty()){
                                 strCreditAmount="0.0";
                            }if (strDebitAmount.isEmpty()){
                                strDebitAmount="0.0";
                            }
                                dialog.dismiss();
                            if (strDate.isEmpty()){

                                strDate="";
                            }

                                updateData(sd.getTOKEN(),strDate,strParticular,strDebitAmount,strCreditAmount,list.get(id).getId());
                            //}
                        }
                    });
                    alert = new AlertDialog.Builder(SingleDataListActivity.this);
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    //alert.setCancelable(false);

                    dialog = alert.create();
                    dialog.show();
                }
            });

            btnDeleteData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(SingleDataListActivity.this,"ID + "+list.get(id).getPos() +" UserID "+list.get(id).getId(),Toast.LENGTH_SHORT).show();
                    new android.app.AlertDialog.Builder(SingleDataListActivity.this)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this row?")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            actionDialog.dismiss();
                                            deleteData(sd.getTOKEN(),list.get(id).getId());

                                        }
                                    }).setNegativeButton("No", null).show();
                }
            });
            // this is set the view from XML inside AlertDialog
            actionAlert.setView(actionAlertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            //alert.setCancelable(false);

            actionDialog = actionAlert.create();
            actionDialog.show();





        }
    }
}
