package com.books.bookify;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comix.rounded.RoundedCornerImageView;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScannedBookDetails extends AppCompatActivity {
    private OkHttpClient client;
    MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/png");
    private String isbn_code;
    ArrayList<String> arrayList;
  private RoundedCornerImageView imageView;
    ArrayList<Image> imagesList;
    Image image ;
    Image image_1 ;
    Image image_2 ;
    Image image_3 ;
    String selectedCategory;
    ProgressBar progressBar;
    Button upload;
    EditText title,description,price;
    RoundedCornerImageView uploadImages,image1,image2,image3,image4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_book_details);
        client = new OkHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        arrayList = new ArrayList<>();
        arrayList.add("Arts & Music");
        arrayList.add("Biographies");
        arrayList.add("Business");
        arrayList.add("Kids");
        arrayList.add("Comics");
        arrayList.add("Computer & Tech");
        arrayList.add("Cooking");
        arrayList.add("Hobbies & Crafts");
        arrayList.add("Edu & References");
        arrayList.add("Health & Fitness");
        arrayList.add("Science & Maths");
        arrayList.add("Sci-Fi & Fantasy");
        arrayList.add("Romance");
        arrayList.add("Religion");
        arrayList.add("Sports");
        MaterialSpinner spinner =findViewById(R.id.spinner);
        spinner.setItems(arrayList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selectedCategory = item;
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        upload = findViewById(R.id.upload);
        progressBar = findViewById(R.id.progress);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray =this.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        uploadImages = findViewById(R.id.upload_images);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3= findViewById(R.id.image3);
        image4= findViewById(R.id.image4);
        image1.setBackgroundResource(backgroundResource);
        image2.setBackgroundResource(backgroundResource);
        image3.setBackgroundResource(backgroundResource);
        image4.setBackgroundResource(backgroundResource);
        uploadImages.setBackgroundResource(backgroundResource);
        imageView = findViewById(R.id.book_thumbnail);
        imageView.setOnLongClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.zoomed_image_on_longpress);
            RoundedCornerImageView roundedCornerImageView = dialog.findViewById(R.id.zoomed_image);
            progressBar.setVisibility(View.VISIBLE);
            Glide
                    .with(this)
                    .load(((BitmapDrawable)imageView.getDrawable()).getBitmap())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .useAnimationPool(true)
                    .into(roundedCornerImageView);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return true;
        });
        image1.setOnClickListener(v -> {

            Glide
                    .with(this)
                    .load(image.imagePath)
                    .centerCrop()
                    .useAnimationPool(true)
                    .into(imageView);
        });
        image2.setOnClickListener(v -> {
            Glide
                    .with(this)
                    .load(image_1.imagePath)
                    .centerCrop()
                    .fitCenter()
                    .useAnimationPool(true)
                    .into(imageView);
        });
        image3.setOnClickListener(v -> {
            Glide
                    .with(this)
                    .load(image_2.imagePath)
                    .centerCrop()
                    .fitCenter()
                    .useAnimationPool(true)
                    .into(imageView);
        });
        image4.setOnClickListener(v -> {
            Glide
                    .with(this)
                    .load(image_3.imagePath)
                    .centerCrop()
                    .fitCenter()
                    .useAnimationPool(true)
                    .into(imageView);
        });
        Intent intent = getIntent();
        isbn_code = intent.getStringExtra("ISBN_CODE");
        try {
            String response = run("https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn_code);
            Log.d("Book_DETAILS",""+response);
            JSONObject jsonObject = new JSONObject(response);
          JSONArray jsonObject1 = jsonObject.getJSONArray("items");
          JSONObject jsonObject2 = jsonObject1.getJSONObject(0);
          JSONObject jsonObject3 = jsonObject2.getJSONObject("volumeInfo");
          JSONObject jsonObject4 = jsonObject3.getJSONObject("imageLinks");
          String title = jsonObject3.getString("title");
          toolbar.setTitle(title);
          String image_url = jsonObject4.getString("thumbnail");
            Glide
                    .with(this)
                    .load(image_url)
                    .fitCenter()
                    .useAnimationPool(true)
                    .into(imageView);

            Log.d("Book_TITLE"," "+jsonObject4);
        } catch (JSONException |IOException e) {
            e.printStackTrace();
        }
        uploadImages.setOnClickListener(v -> {
            Intent intent1= new Intent(this, GalleryActivity.class);
            Params params = new Params();
            params.setCaptureLimit(4);
            params.setPickerLimit(4);
            params.setToolbarColor(getColor(R.color.colorPrimary));
            params.setActionButtonColor(getColor(R.color.unselected_color));
            params.setButtonTextColor(getColor(R.color.custom_color));
            intent1.putExtra(Constants.KEY_PARAMS, params);
            startActivityForResult(intent1, Constants.TYPE_MULTI_PICKER);
        });
        upload.setOnClickListener(v -> {
            if (TextUtils.isEmpty(title.getText())){
                Toast.makeText(this, "Book Title is Mandatory", Toast.LENGTH_SHORT).show();
                title.requestFocus();
                return;
            }
           else  if (TextUtils.isEmpty(price.getText().toString()) || Integer.parseInt(price.getText().toString())<100) {
                Toast.makeText(this, "Minimum price is Rs 100", Toast.LENGTH_SHORT).show();
                price.requestFocus();
            }
            else if (TextUtils.isEmpty(description.getText())){
                Toast.makeText(this, "Description is Mandatory", Toast.LENGTH_SHORT).show();
                description.requestFocus();
                return;
            }
            else {
                try {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("images", title.getText().toString()+"1.png", RequestBody.create(new File(image.imagePath), MEDIA_TYPE_PNG))
                            .addFormDataPart("images", title.getText().toString()+"2.png", RequestBody.create(new File(image_1.imagePath), MEDIA_TYPE_PNG))
                            .addFormDataPart("images", title.getText().toString()+"3.png", RequestBody.create(new File(image_2.imagePath), MEDIA_TYPE_PNG))
                            .addFormDataPart("images", title.getText().toString()+"4.png", RequestBody.create(new File(image_3.imagePath), MEDIA_TYPE_PNG))
                            .addFormDataPart("username",getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("username",null) )
                            .addFormDataPart("title",title.getText().toString())
                            .addFormDataPart("category",selectedCategory)
                            .addFormDataPart("price",price.getText().toString())
                            .addFormDataPart("description",description.getText().toString())
                            .addFormDataPart("date",new Date().toString())
                            .addFormDataPart("isbn",isbn_code)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://192.168.43.168:8080/uploadbook")
                            .post(requestBody)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        Log.d("UPLOAD RESPONSE",""+response.body().string());
                        startActivity(new Intent(ScannedBookDetails.this,MainActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                try {

                    ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                    Log.d("images-1", imagesList.get(0) + "");
                    Glide
                            .with(this)
                            .load(imagesList.get(0))
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image1);
                    Glide
                            .with(this)
                            .load(imagesList.get(1))
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image2);
                    Glide
                            .with(this)
                            .load(imagesList.get(2))
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image3);
                    Glide
                            .with(this)
                            .load(imagesList.get(3))
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image4);
                    break;

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            case  Constants.TYPE_MULTI_PICKER:
                try {

                     imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                     image = imagesList.get(0);
                     image_1 = imagesList.get(1);
                     image_2 = imagesList.get(2);
                     image_3 = imagesList.get(3);
                    Log.d("images-1",  image.imagePath + "");
                    Glide
                            .with(this)
                            .load(image.imagePath)
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image1);
                    Glide
                            .with(this)
                            .load(image_1.imagePath)
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image2);
                    Glide
                            .with(this)
                            .load(image_2.imagePath)
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image3);
                    Glide
                            .with(this)
                            .load(image_3.imagePath)
                            .centerCrop()
                            .useAnimationPool(true)
                            .into(image4);
                    break;

                }
                catch (Exception e){
                    e.printStackTrace();
                }
        }
    }
    String post(String url,JSONObject jsonObject) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
