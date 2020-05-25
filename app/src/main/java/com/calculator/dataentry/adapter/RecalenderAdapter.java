package com.calculator.dataentry.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.activity.ReCalenderActivity;
import com.calculator.dataentry.activity.ReLedgerActivity;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.SingleUserModel;
import com.calculator.dataentry.model.UserModel;

import java.util.ArrayList;

public class RecalenderAdapter  extends ArrayAdapter {

    ArrayList<SingleUserModel.userDetails> al = null;
    Context context;
    private ReCalenderActivity reCalenderActivity;
    SessionManagment sd;


    public RecalenderAdapter(Context context, ArrayList<SingleUserModel.userDetails> al) {
        super(context, R.layout.event_item_layout, al);
        this.context=context;
        this.reCalenderActivity=(ReCalenderActivity)context;
        this.al = al;
        sd = new SessionManagment(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        final int pos = position;
        final SingleUserModel.userDetails lm = al.get(pos);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_item_layout, parent, false);
            vh.tvName=(TextView) convertView.findViewById(R.id.tvName);
            vh.tvdate=(TextView) convertView.findViewById(R.id.tvdate);
            vh.tvDelet=(TextView) convertView.findViewById(R.id.tvDelete);
            vh.tvrestore=(TextView) convertView.findViewById(R.id.tvRestore);

            convertView.setTag(vh);
        } else {
            vh =(ViewHolder)convertView.getTag();
        }

        vh.tvName.setText(lm.event_title);
        vh.tvdate.setText(lm.event_date);
        vh.tvDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this row ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     reCalenderActivity.deleteData(sd.getTOKEN(),lm.id,pos,"2");
                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        vh.tvrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Restore")
                        .setMessage("Are you sure you want to restore this row ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reCalenderActivity.RestoreData(sd.getTOKEN(),lm.id,pos,"0");

                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvName,tvdate,tvDelet,tvrestore;
    }
}