package com.calculator.dataentry.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.activity.DataListActivity;
import com.calculator.dataentry.activity.SingleDataListActivity;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ViewHolder>{
    private List<UserModel.userDetails> listdata;
    private List<UserModel.userDetails> searchList;
    private DataListActivity dataListActivity;
    private Context context;
    private AlertDialog dialog;
    private AlertDialog.Builder  alert;
    SessionManagment sd;

    // RecyclerView recyclerView;
    public DataListAdapter(Context context, List<UserModel.userDetails> listdata) {
        this.context = context;
        this.dataListActivity = (DataListActivity) context;
        this.listdata = listdata;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(listdata);
        sd = new SessionManagment(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.datalist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final UserModel.userDetails myListData = listdata.get(position);
        holder.txtName.setText(myListData.name);
        holder.txtMobile.setText(myListData.mobile);
        holder.txtAmount.setText(myListData.totalamount);
        holder.delelteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this row ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataListActivity.deleteData(sd.getTOKEN(),listdata.get(position).id,position,"1");

                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        holder.editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                final EditText edtName = (EditText) alertLayout.findViewById(R.id.edtName);
                final EditText  edtMobile = (EditText) alertLayout.findViewById(R.id.edtMobile);
                final EditText  edtEmail = (EditText) alertLayout.findViewById(R.id.edtEmail);
                edtName.setText(listdata.get(position).name);
                edtEmail.setText(listdata.get(position).email);
                edtMobile.setText(listdata.get(position).mobile);
                Button btnSendData = (Button) alertLayout.findViewById(R.id.btnSendData);
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
                            dialog.dismiss();
                          dataListActivity.updateData(sd.getTOKEN(),listdata.get(position).id,strName, strMobile, strEmail,position);
                        }
                    }
                });
                  alert = new AlertDialog.Builder(context);
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                //alert.setCancelable(false);
                 dialog = alert.create();
                dialog.show();
            }
        });
        holder.linearId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();

                Intent i=new Intent(context, SingleDataListActivity.class);

                i.putExtra("id",myListData.id);
                i.putExtra("name",myListData.name);
                i.putExtra("mobile",myListData.mobile);
                i.putExtra("email",myListData.email);

                context.startActivity(i);
            }
        });
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listdata.clear();
        if (charText.length() == 0) {
            listdata.addAll(searchList);
        } else {
            for (UserModel.userDetails wp : searchList) {
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    listdata.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageProfile,editDetails,delelteDetails;
        public TextView txtName,txtMobile,txtAmount;
        public LinearLayout linearId;
        public ViewHolder(View itemView) {
            super(itemView);
            this.delelteDetails = (ImageView) itemView.findViewById(R.id.delelteDetails);
            this.editDetails = (ImageView) itemView.findViewById(R.id.editDetails);
            this.imageProfile = (ImageView) itemView.findViewById(R.id.imageProfile);
            this.txtName = (TextView) itemView.findViewById(R.id.txtName);
            this.txtMobile = (TextView) itemView.findViewById(R.id.txtMobile);
            this.txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            this.linearId = (LinearLayout)itemView.findViewById(R.id.linearId);
        }
    }
}
