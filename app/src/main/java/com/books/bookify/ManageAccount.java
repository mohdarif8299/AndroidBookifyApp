package com.books.bookify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManageAccount extends AppCompatActivity {
    CircleImageView imgProfile;
    SharedPreferences sharedPref;
    private OkHttpClient client;
    Uri imageUri;
    String image_local_profile;
    String imagePath;
    SwipeRefreshLayout refreshLayout;
    MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/png");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray =this.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        client = new OkHttpClient();
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(()->{
            if (sharedPref.getString("profile",null)==null)  {
                refreshLayout.setRefreshing(false);
                return;
            }
            System.out.println(sharedPref.getString("profile",null));
            byte [] encodeByte=Base64.decode(sharedPref.getString("profile",null),Base64.DEFAULT);
            InputStream inputStream1  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap1  = BitmapFactory.decodeStream(inputStream1);
            Glide.with(this).
                    load(bitmap1)
                    .centerCrop()
                    .into(imgProfile);
            refreshLayout.setRefreshing(false);
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Your Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imgProfile = findViewById(R.id.imageProfile);
        LinearLayout manageAccount = findViewById(R.id.manage_account);
        manageAccount.setBackgroundResource(backgroundResource);
        manageAccount.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
                if (resultcode == Activity.RESULT_OK) {
                    try {
                        imageUri = data.getData();
                        imagePath = imageUri.getPath();
                        Log.d("IMAGE_PATH", "" + imagePath);
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("image", sharedPref.getString("username", null)+".png", RequestBody.create(new File(imagePath), MEDIA_TYPE_PNG))
                                .addFormDataPart("username", sharedPref.getString("username", null))
                                .build();
                        Request request = new Request.Builder()
                                .url("http://192.168.43.168:5000/upload_profile_pic/")
                                .post(requestBody)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            InputStream inputStream = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                            byte[] arr = baos.toByteArray();
                            image_local_profile =  Base64.encodeToString(arr, Base64.DEFAULT);
                            sharedPref.edit().putString("profile",image_local_profile).commit();
                            byte [] encodeByte=Base64.decode(image_local_profile,Base64.DEFAULT);
                            InputStream inputStream1  = new ByteArrayInputStream(encodeByte);
                            Bitmap bitmap1  = BitmapFactory.decodeStream(inputStream1);
                            Glide.with(this).
                                    load(bitmap1)
                                    .centerCrop()
                                    .into(imgProfile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    }
    String post(String url,JSONObject jsonObject) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshLayout = findViewById(R.id.refresh);
        imgProfile = findViewById(R.id.imageProfile);
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        try {
            byte[] encodeByte = Base64.decode(sharedPref.getString("profile", null), Base64.DEFAULT);
            InputStream inputStream1 = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream1);
            Glide.with(this).
                    load(bitmap1)
                    .centerCrop()
                    .into(imgProfile);
        }
        catch (Exception e) {}

    }
}
