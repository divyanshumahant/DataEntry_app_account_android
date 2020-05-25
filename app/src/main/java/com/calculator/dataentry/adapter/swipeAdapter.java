package com.calculator.dataentry.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.calculator.dataentry.Interface.UpdateInterface;
import com.calculator.dataentry.R;
import com.calculator.dataentry.activity.DataListActivity;
import com.calculator.dataentry.activity.swipeActivity;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.common.TouchImageView;
import com.calculator.dataentry.model.SingleUserModel;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class swipeAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ConnectionDetector cd ;
    ProgressDialog pd ;
    APIInterface apiInterface ;
    UpdateInterface updateInterface;
    private ArrayList<SingleUserModel.userDetails> imageModelList;
    int possss=0 ;
    String baseurl="";
    SessionManagment sd;
    private swipeActivity swipeActivity;

    public swipeAdapter(Context context, ArrayList<SingleUserModel.userDetails> feedItemList,String baseurl) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageModelList = feedItemList;
        this.baseurl= baseurl;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        cd = new ConnectionDetector(context);
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        sd = new SessionManagment(context);
        updateInterface =(UpdateInterface)context;
        this.swipeActivity = (swipeActivity) context;
    }

    @Override
    public int getCount() {
        return (null != imageModelList ? imageModelList.size() : 0);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final SingleUserModel.userDetails get = imageModelList.get(position);
        TouchImageView photoView = (TouchImageView) itemView.findViewById(R.id.photo_view);
        TextView tvDelet = (TextView)itemView.findViewById(R.id.tvDelet);

        Picasso.with(mContext).load(baseurl+get.image).into(photoView);

        tvDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Delete.....")
                        .setMessage("Are you sure you want to delete this image ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        swipeActivity.getDeletImage(sd.getTOKEN(),get.id,get.image,"1",position,get.user_id);
                                        new Handler().postDelayed(new Runnable() {
                                            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
                                            @Override
                                            public void run() {
                                                updateInterface.updatedate();
                                            }
                                        }, 500);
                                    }
                                }).setNegativeButton("No", null).show();
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


    public void getDeletImage(String token,String id,String image,String sta,String uid) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(mContext, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getDeletImage(token,id,image,sta,uid);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        if(resource.status.equals("succes")){
                            Toast.makeText(mContext, "Delete Successfully", Toast.LENGTH_SHORT).show();
                          // remove(possss);
                        }else{
                            Toast.makeText(mContext, "Sorry, try after some time", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(mContext, "please try again...!", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(mContext, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

}