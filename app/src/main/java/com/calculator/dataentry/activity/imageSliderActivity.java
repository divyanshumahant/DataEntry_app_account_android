package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.R;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.SingleUserModel;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;

public class imageSliderActivity extends AppCompatActivity {

   ImageView img ;
   ConnectionDetector cd;
   ProgressDialog pd;
   TextView tvDelet;
   APIInterface apiInterface;
   String id="", image="";
   SessionManagment sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimage);

        img = (ImageView)findViewById(R.id.imgFullView);
        tvDelet=(TextView)findViewById(R.id.tvDelete);
        cd = new ConnectionDetector(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait....");
        pd.setCancelable(false);
        cd = new ConnectionDetector(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sd = new SessionManagment(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Image");

        Intent in = getIntent();
        id =  in.getStringExtra("id");
        image = in.getStringExtra("image");

        byte[] imageAsBytes = Base64.decode(image, Base64.DEFAULT);
        img.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        tvDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getDeletImage(sd.getTOKEN(),id,image,"1","");
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

    public void getDeletImage(String token,String id,String image,String sta,String uid) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(imageSliderActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(imageSliderActivity.this, "Delet Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(imageSliderActivity.this, "Sorry, try after some time", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(imageSliderActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(imageSliderActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

  /*   if (requestCode == REQUEST_TAKE_PHOTO)
    {
        Bitmap bitmap = camera.getCameraBitmap();
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            baseimg = Base64.encodeToString(b, Base64.DEFAULT);
            getUploadImage(id,baseimg);
        } else {
            Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
        }
    }
*/


}
