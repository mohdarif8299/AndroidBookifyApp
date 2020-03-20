package com.books.bookify.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.books.bookify.ItemOffsetDecoration;
import com.books.bookify.R;
import com.books.bookify.adapters.BooksAdapter;
import com.books.bookify.models.BookAvailable;
import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.synnapps.carouselview.CarouselView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import okio.BufferedSource;
import okio.ByteString;

public class HomeFragment extends Fragment {
    private Context context;
    CarouselView carouselView;
    CircleImageView imgProfile;
    SharedPreferences sharedPref;
    private OkHttpClient client;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    List<BookAvailable> list;
    TextView title;
    View root;
    MediaType MEDIA_TYPE = MediaType.parse("application/json");
    int[] sampleImages = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4,};
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        imgProfile = root.findViewById(R.id.imageProfile);
        title = root.findViewById(R.id.title);
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        list = new ArrayList<>();
        client = new OkHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int backgroundResource = typedArray.getResourceId(0, 0);
        refreshLayout = root.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(() -> {
            list.clear();
                     loadData(recyclerView,list);
                    refreshLayout.setRefreshing(false);
                }
        );
        carouselView = root.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(((position, imageView) -> {
            imageView.setBackgroundResource(sampleImages[position]);
        }));
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        loadData(recyclerView, list);
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        title.setText(sharedPref.getString("displayname", null));
     try {
         byte[] encodeByte = Base64.decode(sharedPref.getString("profile", null), Base64.DEFAULT);
         InputStream inputStream1 = new ByteArrayInputStream(encodeByte);
         Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream1);
         Glide.with(this).
                 load(bitmap1)
                 .centerCrop()
                 .into(imgProfile);
     }
     catch (Exception e){}

    }
    private void loadData(RecyclerView recyclerView, List<BookAvailable> list) {
        try {
            Request request = new Request.Builder()
                    .url("https://enigmatic-castle-41717.herokuapp.com/getallbooks")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
                JSONArray jsonArray  = new JSONArray(response.body().string());
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new BookAvailable(jsonObject.getString("title"),jsonObject.getString("image"),jsonObject.getString("price")));
                    BooksAdapter booksAdapter = new BooksAdapter(list,context);
                    recyclerView.setAdapter(booksAdapter);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}