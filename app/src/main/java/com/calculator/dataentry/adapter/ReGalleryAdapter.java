package com.calculator.dataentry.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.activity.ReGalleryActivity;
import com.calculator.dataentry.activity.ReLedgerActivity;
import com.calculator.dataentry.activity.RecyclePhotoGallery;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.UserModel;

import java.util.ArrayList;

public class ReGalleryAdapter extends ArrayAdapter {

    ArrayList<UserModel.userDetails> al = null;
    Context context;
    private ReGalleryActivity reGalleryActivity;
    SessionManagment sd;


    public ReGalleryAdapter(Context context, ArrayList<UserModel.userDetails> al) {
        super(context, R.layout.ledger_itemlayout, al);
        this.context=context;
        this.reGalleryActivity=(ReGalleryActivity)context;
        this.al = al;
        sd = new SessionManagment(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        int pos = position;
        final UserModel.userDetails lm = al.get(pos);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ledger_itemlayout, parent, false);
            vh.imgRestore=(ImageView)convertView.findViewById(R.id.imgrestore);
            vh.imgDelet=(ImageView)convertView.findViewById(R.id.delelteDetails);
            vh.tvName=(TextView) convertView.findViewById(R.id.tvName);
            vh.tvMobile=(TextView) convertView.findViewById(R.id.tvMobile);
            vh.cardView=(CardView)convertView.findViewById(R.id.cardContainer);

            convertView.setTag(vh);
        } else {
            vh =(ViewHolder)convertView.getTag();
        }

        vh.tvName.setText(lm.name);
        vh.tvMobile.setText(lm.mobile);
        vh.imgDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this row ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reGalleryActivity.deleteData(sd.getTOKEN(),al.get(position).id,position,"2");

                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        vh.imgRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Restore")
                        .setMessage("Are you sure you want to restore this row ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reGalleryActivity.RestoreData(sd.getTOKEN(),al.get(position).id,position,"0");

                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, RecyclePhotoGallery.class);
                in.putExtra("id",lm.id);
                context.startActivity(in);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView imgRestore,imgDelet;
        TextView tvName,tvMobile;
        CardView cardView;
    }
}