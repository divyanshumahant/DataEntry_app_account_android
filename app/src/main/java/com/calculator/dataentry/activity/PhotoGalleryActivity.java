package com.calculator.dataentry.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.Interface.UpdateInterface;
import com.calculator.dataentry.R;
import com.calculator.dataentry.adapter.ShowImageAdapter;
import com.calculator.dataentry.common.APIClient;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;
import com.calculator.dataentry.common.SessionManagment;
import com.calculator.dataentry.model.PhotoModel;
import com.calculator.dataentry.model.SingleUserModel;
import com.google.gson.JsonElement;
import com.mindorks.paracamera.Camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class PhotoGalleryActivity extends AppCompatActivity {

    CircleImageView img ;
    GridView gridView;
    private String lastDate="";
    ArrayList<PhotoModel> list;
    ProgressDialog pd;
    String id = "",strName="",strEmail="",strMobile="";
    APIInterface apiInterface;
    ConnectionDetector cd;
    ShowImageAdapter adapter;
    private static final int CAMERA_REQUEST = 1888;
    LinearLayout linearLayout;
    String baseimg="";
    Camera camera;
    SessionManagment sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layout);

        img = (CircleImageView)findViewById(R.id.cvImage);
        linearLayout = (LinearLayout)findViewById(R.id.llImage);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait....");
        pd.setCancelable(false);
        cd = new ConnectionDetector(this);
        sd = new SessionManagment(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        list=new ArrayList<>();
        Intent in = getIntent();
        id =   in.getStringExtra("id");
        strName=getIntent().getStringExtra("name");
        strEmail=getIntent().getStringExtra("email");
        strMobile=getIntent().getStringExtra("mobile");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(strName);

       /* camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
*/
        gridView=(GridView)findViewById(R.id.gridId);
        // @SuppressLint("WrongConstant") LinearLayoutManager layoutManager
        //           = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
       /* recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(PhotoGalleryActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {
            getUserImage(sd.getTOKEN(),id,"0");
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
 Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                cameraUpView();
            }
        });
    }

    private void cameraUpView() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            try {
                Bitmap photo = camera.getCameraBitmap();

//            Bitmap photo = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(photo);
                // CALL THIS METHOD TO GET THE ACTUAL PATH

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 25, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                baseimg = Base64.encodeToString(b, Base64.DEFAULT);
                getUploadImage(sd.getTOKEN(),id,baseimg);
                Uri selectedImage = getImageUri(photo);
                //Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                //  Toast.makeText(PhotoGalleryActivity.this,picturePath,Toast.LENGTH_SHORT).show();

                File imagefile = new File(picturePath);
                if (imagefile.exists()) {
                    delete(PhotoGalleryActivity.this,imagefile);
                }
            }catch (Exception e){
                e.printStackTrace();
          }
        }
    }


    private Uri getImageUri(Bitmap inImage) {
        Log.d("Image-uri", "call");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[] {
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, where, selectionArgs);
        if (file.exists()) {
            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }


    public void getUploadImage(String token,final String idd, final String img) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(PhotoGalleryActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getUploadImage(token,idd,img);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        PhotoModel photoModel = new PhotoModel();

                        if(resource.status.equals("succes")){
                            Toast.makeText(PhotoGalleryActivity.this, "Upload image successfully", Toast.LENGTH_SHORT).show();
                            JsonElement id=resource.data.get("insertId");

                            photoModel.setId(id.getAsString());
                            photoModel.setImage(resource.imageUrl);
                            photoModel.setUser_id(idd);
                            list.add(photoModel);
                            adapter = new ShowImageAdapter(PhotoGalleryActivity.this, list);
                            gridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(PhotoGalleryActivity.this, "Sorry, try after sometime", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(PhotoGalleryActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(PhotoGalleryActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    public void getUpdatePhotoGallery(String token,String id,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(PhotoGalleryActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            // pd.show();
            Call<SingleUserModel> call = apiInterface.getImage(token,id,"0");
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        list.clear();
                        // pd.dismiss();
                        SingleUserModel resource = response.body();
                        for (int i = 0; i < resource.userList.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            String dateTime = resource.userList.get(i).datetime.substring(0, 10);
                            if (!lastDate.equalsIgnoreCase(dateTime)) {
                                photoModel.setDatetime(dateTime);
                                lastDate = dateTime;
                            } else {
                                photoModel.setDatetime("");
                            }
                            photoModel.setId(resource.userList.get(i).id);
                            photoModel.setImage(resource.imageUrl+resource.userList.get(i).image);
                            photoModel.setUser_id(resource.userList.get(i).user_id);
                            list.add(photoModel);
                        }
                        if (resource.status.equals("succes")) {
                            adapter = new ShowImageAdapter(PhotoGalleryActivity.this, list);
                            gridView.setAdapter(adapter);
                        } else if (resource.status.equals("noData")) {
                            Toast.makeText(PhotoGalleryActivity.this, "No image exists", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(PhotoGalleryActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    public void getUserImage(String token,String id,String sta) {
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(PhotoGalleryActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            Call<SingleUserModel> call = apiInterface.getImage(token,id,sta);
            call.enqueue(new Callback<SingleUserModel>() {
                @Override
                public void onResponse(Call<SingleUserModel> call, retrofit2.Response<SingleUserModel> response) {
                    try {
                        pd.dismiss();
                        SingleUserModel resource = response.body();
                        for (int i=0;i<resource.userList.size();i++){
                            PhotoModel photoModel=new PhotoModel();
                            String dateTime =resource.userList.get(i).datetime.substring(0,10);
                            if (!lastDate.equalsIgnoreCase(dateTime)){
                                photoModel.setDatetime(dateTime);
                                lastDate=dateTime;

                            }else {
                                photoModel.setDatetime("");
                            }
                            photoModel.setId(resource.userList.get(i).id);
                            photoModel.setImage(resource.imageUrl+resource.userList.get(i).image);
                            photoModel.setUser_id(resource.userList.get(i).user_id);
                            list.add(photoModel);
                        }
                        if(resource.status.equals("succes")){
                            adapter = new ShowImageAdapter(PhotoGalleryActivity.this,list);
                            adapter.notifyDataSetChanged();
                            gridView.setAdapter(adapter);
                        }else if(resource.status.equals("noData")){
                          //  Toast.makeText(PhotoGalleryActivity.this, "No image exists", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        pd.dismiss();
                    }

                }
                @Override
                public void onFailure(Call<SingleUserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(PhotoGalleryActivity.this, "please try again... !", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUpdatePhotoGallery(sd.getTOKEN(),id,"0");
        //  getUserImage(id);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    /*public void jsonData(final String id) {
        pd.show();
        String url = "http://tranquil-ravine-48528.herokuapp.com/getImage";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            list.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray users = jsonObject.getJSONArray("result");
                            for (int i = 0; i <= users.length(); i++) {
                                JSONObject jo = users.getJSONObject(i);
                                date = jo.getString("datetime");
                                list.add(new CategoryEvent(null,null,null,date,CategoryEvent.DATE_TYPE));

                                try {
                                    JSONArray user2 = jo.getJSONArray("item");
                                    for (int j = 0; j < user2.length(); j++) {
                                        JSONObject jo2 = user2.getJSONObject(j);
                                        imgid = jo2.getString("id");
                                        image = jo2.getString("image");
                                        uid = jo2.getString("user_id");
                                        list.add(new CategoryEvent(uid,imgid,image,null,CategoryEvent.IMAGE_TYPE));
                                    }

                                } catch (Exception e) {
                                    pd.dismiss();
                                    // Toast.makeText(RestDiffActivity.this, "Error" + e, Toast.LENGTH_LONG).show();
                                }

                                DifferentRowAdapter adapter = new DifferentRowAdapter(list, PhotoGalleryActivity.this);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            //  Toast.makeText(RestDiffActivity.this, "Error" + e, Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(PhotoGalleryActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(PhotoGalleryActivity.this);
        request.setShouldCache(false);
        queue.add(request);
        queue.getCache().clear();
        queue.add(request);
    }
*/

/*
    public void showdialog() {
        final Dialog dialog = new Dialog(PhotoGalleryActivity.this);
        dialog.setContentView(R.layout.photo_layout);
        dialog.show();

        final TextView etDate = (TextView) dialog.findViewById(R.id.etDate);
        final CircleImageView img = (CircleImageView) dialog.findViewById(R.id.profile_image);
        final Calendar cal;
        final DatePickerDialog.OnDateSetListener dat;
        cal = Calendar.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String strDob = String.valueOf(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.getTime()));
                if (strDob.equals(BuildConfig.FLAVOR)) {
                    strDob = BuildConfig.FLAVOR;
                }
                etDate.setText(strDob);
                eventdate = strDob;
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PhotoGalleryActivity.this, dat,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
*/



}



