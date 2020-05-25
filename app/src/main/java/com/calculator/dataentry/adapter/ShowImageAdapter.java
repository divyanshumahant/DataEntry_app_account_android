package com.calculator.dataentry.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.model.PhotoModel;
import com.calculator.dataentry.activity.swipeActivity;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class ShowImageAdapter extends ArrayAdapter {

    ArrayList<PhotoModel> al = null;
    Context context;

    public ShowImageAdapter(Context context, ArrayList<PhotoModel> al) {
        super(context, R.layout.show_image_item, al);
        this.context=context;
        this.al = al;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();
        int pos = position;
        final PhotoModel lm = al.get(pos);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.show_image_item, parent, false);
            vh.icon=(ImageView)convertView.findViewById(R.id.userImg);
           // vh.tvDate=(TextView) convertView.findViewById(R.id.tvDate);
            vh.cardImage=(CardView) convertView.findViewById(R.id.cardImage);

            convertView.setTag(vh);
          } else {
            vh =(ViewHolder)convertView.getTag();
         }

        Picasso.with(context).load(lm.image).into(vh.icon);

        vh.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent in = new Intent(context, swipeActivity.class);
                  in.putExtra("id",lm.user_id);
                  in.putExtra("image",lm.image);
                  in.putExtra("pos",String.valueOf(position));
                  context.startActivity(in);
                 // ((Activity)context).finish();
                }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        CardView  cardImage ;
    }
}