package com.calculator.dataentry.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.calculator.dataentry.R;
import com.calculator.dataentry.model.CategoryEvent;
import com.calculator.dataentry.activity.swipeActivity;

import java.util.List;

import static com.calculator.dataentry.model.CategoryEvent.DATE_TYPE;
import static com.calculator.dataentry.model.CategoryEvent.IMAGE_TYPE;

public class DifferentRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryEvent> mList;
    Context context;

    public DifferentRowAdapter(List<CategoryEvent> list, Context context) {
        this.mList = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case DATE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
                return new CityViewHolder(view);
            case IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_image_item, parent, false);
                return new EventViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final CategoryEvent object = mList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case DATE_TYPE:
                    ((CityViewHolder) holder).mTitle.setText(object.getDate());
                    break;
                case IMAGE_TYPE:

                    byte[] imageAsBytes = Base64.decode(object.getImage(), Base64.DEFAULT);
                    ((EventViewHolder) holder).img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0,imageAsBytes.length));

                    ((EventViewHolder) holder).img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                try {
                                    Intent in = new Intent(context, swipeActivity.class);
                                    in.putExtra("id",object.getUserid());
                                    in.putExtra("image",object.getImage());
                                    context.startActivity(in);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Item Not Added. Please Add Again", Toast.LENGTH_SHORT).show();
                                }


                        }
                    });

                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            CategoryEvent object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public CityViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public EventViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.userImg);
        }
    }

}