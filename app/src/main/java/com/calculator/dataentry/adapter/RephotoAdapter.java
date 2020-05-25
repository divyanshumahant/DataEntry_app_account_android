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

import androidx.cardview.widget.CardView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.activity.ReGalleryActivity;
import com.calculator.dataentry.activity.RecyclePhotoGallery;
import com.calculator.dataentry.activity.swipeActivity;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.PhotoModel;
import com.calculator.dataentry.model.SingleUserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RephotoAdapter extends ArrayAdapter {

    ArrayList<SingleUserModel.userDetails> al = null;
    Context context;
    String baseurl="";
    private RecyclePhotoGallery recyclePhotoGallery;
    SessionManagment sd;

    public RephotoAdapter(Context context, ArrayList<SingleUserModel.userDetails> al,String baseurl) {
        super(context, R.layout.re_photo_layout, al);
        this.context=context;
        this.al = al;
        this.baseurl=baseurl;
        recyclePhotoGallery=(RecyclePhotoGallery)context;
        sd=new SessionManagment(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        final int pos = position;
        final SingleUserModel.userDetails lm = al.get(pos);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.re_photo_layout, parent, false);
            vh.icon=(ImageView)convertView.findViewById(R.id.userImg);
            vh.imgDelet=(ImageView)convertView.findViewById(R.id.imgDelet);
            vh.imgRestore=(ImageView)convertView.findViewById(R.id.imgRestore);

            Picasso.with(context).load(baseurl+lm.image).into(vh.icon);

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
                                            recyclePhotoGallery.deleteData(sd.getTOKEN(),lm.id,lm.image,pos,"2",lm.user_id);

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
                                            recyclePhotoGallery.RestoreData(sd.getTOKEN(),lm.id,lm.image,pos,"0",lm.user_id);
                                        }
                                    }).setNegativeButton("No", null).show();

                }
            });

            convertView.setTag(vh);
        } else {
            vh =(ViewHolder)convertView.getTag();
        }


        return convertView;
    }

    class ViewHolder {
        ImageView icon,imgDelet,imgRestore;
    }
}