package com.books.bookify.user;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.books.bookify.R;

public class UserUploadBooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upload_books);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray =this.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Uploaded Books");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView imageView = findViewById(R.id.popup_menu);
        imageView.setBackgroundResource(backgroundResource);
        imageView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, imageView);
            popup.getMenuInflater().inflate(R.menu.menu_manage_account,popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) { return true;
                }
            });

            popup.show();//showing popup menu
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
