package com.books.bookify;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.books.bookify.ui.account.AccountFragment;
import com.books.bookify.ui.cart.CartFragment;
import com.books.bookify.ui.home.HomeFragment;
import com.books.bookify.ui.search.SearchFragment;
import com.github.florent37.expansionpanel.ExpansionLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout home_layout,search_layout,cart_layout,account_layout,sell_layout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray =this.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        home_layout = findViewById(R.id.home);
        search_layout = findViewById(R.id.search);
        cart_layout = findViewById(R.id.cart);
        sell_layout = findViewById(R.id.sell);
        account_layout  =  findViewById(R.id.account);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new HomeFragment());
        fragmentTransaction.commit();
        home_layout.setOnClickListener(view->{
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,new HomeFragment());
            fragmentTransaction.commit();
            ImageView imageView = findViewById(R.id.home_icon);
            imageView.setBackground(getDrawable(R.drawable.home));
            TextView textView = findViewById(R.id.home_text);
            textView.setTextColor(getColor(R.color.colorPrimary));
            TextView textView1 = findViewById(R.id.search_text);
            textView1.setTextColor(Color.BLACK);
            ImageView sellIcon = findViewById(R.id.sell_icon);
            sellIcon.setBackground(getDrawable(R.drawable.unselected_sell_icon));
            TextView sellText = findViewById(R.id.sell_text);
            sellText.setTextColor(Color.BLACK);
            ImageView imageView1 = findViewById(R.id.search_icon);
            imageView1.setBackground(getDrawable(R.drawable.search));
            ImageView imageView3 = findViewById(R.id.account_icon);
            imageView3.setBackground(getDrawable(R.drawable.account));
            TextView textView4 = findViewById(R.id.account_text);
            textView4.setTextColor(Color.BLACK);
            TextView textView2 = findViewById(R.id.cart_text);
            textView2.setTextColor(Color.BLACK);
            ImageView imageView2 = findViewById(R.id.cart_icon);
            imageView2.setBackground(getDrawable(R.drawable.cart));
        });
        home_layout.setBackgroundResource(backgroundResource);
        search_layout.setOnClickListener(view->{
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,new SearchFragment());
            fragmentTransaction.commit();
            ImageView imageView = findViewById(R.id.home_icon);
            imageView.setBackground(getDrawable(R.drawable.home_unselected));
            TextView textView = findViewById(R.id.home_text);
            textView.setTextColor(Color.BLACK);
            ImageView sellIcon = findViewById(R.id.sell_icon);
            sellIcon.setBackground(getDrawable(R.drawable.unselected_sell_icon));
            TextView sellText = findViewById(R.id.sell_text);
            sellText.setTextColor(Color.BLACK);
            ImageView imageView1 = findViewById(R.id.search_icon);
            imageView1.setBackground(getDrawable(R.drawable.search_selected));
            TextView textView1 = findViewById(R.id.search_text);
            textView1.setTextColor(getColor(R.color.colorPrimary));
            ImageView imageView3 = findViewById(R.id.account_icon);
            imageView3.setBackground(getDrawable(R.drawable.account));
            TextView textView4 = findViewById(R.id.account_text);
            textView4.setTextColor(Color.BLACK);
            TextView textView2 = findViewById(R.id.cart_text);
            textView2.setTextColor(Color.BLACK);
            ImageView imageView2 = findViewById(R.id.cart_icon);
            imageView2.setBackground(getDrawable(R.drawable.cart));
        });
        search_layout.setBackgroundResource(backgroundResource);
        cart_layout.setOnClickListener(view->{
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,new CartFragment());
            fragmentTransaction.commit();
            ImageView sellIcon = findViewById(R.id.sell_icon);
            sellIcon.setBackground(getDrawable(R.drawable.unselected_sell_icon));
            TextView sellText = findViewById(R.id.sell_text);
            sellText.setTextColor(Color.BLACK);
            ImageView imageView = findViewById(R.id.cart_icon);
            imageView.setBackground(getDrawable(R.drawable.cart_selected));
            TextView textView = findViewById(R.id.cart_text);
            textView.setTextColor(getColor(R.color.colorPrimary));
            ImageView imageView2 = findViewById(R.id.home_icon);
            imageView2.setBackground(getDrawable(R.drawable.home_unselected));
            TextView textView3 = findViewById(R.id.home_text);
            textView3.setTextColor(Color.BLACK);
            ImageView imageView3 = findViewById(R.id.account_icon);
            imageView3.setBackground(getDrawable(R.drawable.account));
            TextView textView4 = findViewById(R.id.account_text);
            textView4.setTextColor(Color.BLACK);
            ImageView imageView1 = findViewById(R.id.search_icon);
            imageView1.setBackground(getDrawable(R.drawable.search));
            TextView textView2 = findViewById(R.id.search_text);
            textView2.setTextColor(Color.BLACK);
        });
        cart_layout.setBackgroundResource(backgroundResource);
        account_layout.setOnClickListener(view->{
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,new AccountFragment());
            fragmentTransaction.commit();
            ImageView sellIcon = findViewById(R.id.sell_icon);
            sellIcon.setBackground(getDrawable(R.drawable.unselected_sell_icon));
            TextView sellText = findViewById(R.id.sell_text);
            sellText.setTextColor(Color.BLACK);
            ImageView imageView = findViewById(R.id.account_icon);
            imageView.setBackground(getDrawable(R.drawable.account_selected));
            TextView textView = findViewById(R.id.account_text);
            textView.setTextColor(getColor(R.color.colorPrimary));
            ImageView imageView2 = findViewById(R.id.home_icon);
            imageView2.setBackground(getDrawable(R.drawable.home_unselected));
            TextView textView3 = findViewById(R.id.home_text);
            textView3.setTextColor(Color.BLACK);
            ImageView imageView3 = findViewById(R.id.search_icon);
            imageView3.setBackground(getDrawable(R.drawable.search));
            TextView textView4 = findViewById(R.id.cart_text);
            textView4.setTextColor(Color.BLACK);
            ImageView imageView1 = findViewById(R.id.cart_icon);
            imageView1.setBackground(getDrawable(R.drawable.cart));
            TextView textView2 = findViewById(R.id.search_text);
            textView2.setTextColor(Color.BLACK);
        });
        account_layout.setBackgroundResource(backgroundResource);
        sell_layout.setOnClickListener(v -> {
                Dialog dialog = new Dialog(this,R.style.WideDialog);
                dialog.setContentView(R.layout.dialog_option_for_add_books);
                LinearLayout linearLayout1 = dialog.findViewById(R.id.scan_code);
                linearLayout1.setBackgroundResource(backgroundResource);
                linearLayout1.setOnClickListener(v1 -> {
                    startActivity(new Intent(this, ScanQRCodeActivity.class));
                });
                ExpansionLayout expansionLayout =dialog.findViewById(R.id.expansionLayout);
                expansionLayout.addListener(new ExpansionLayout.Listener() {
                    @Override
                    public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

                    }
                });
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageView sellIcon = findViewById(R.id.sell_icon);
            sellIcon.setBackground(getDrawable(R.drawable.sell_icon));
            TextView sellText = findViewById(R.id.sell_text);
            sellText.setTextColor(getColor(R.color.colorPrimary));
            ImageView imageView = findViewById(R.id.account_icon);
            imageView.setBackground(getDrawable(R.drawable.account));
            TextView textView = findViewById(R.id.account_text);
            textView.setTextColor(getColor(R.color.colorBlack));
            ImageView imageView2 = findViewById(R.id.home_icon);
            imageView2.setBackground(getDrawable(R.drawable.home_unselected));
            TextView textView3 = findViewById(R.id.home_text);
            textView3.setTextColor(Color.BLACK);
            ImageView imageView3 = findViewById(R.id.search_icon);
            imageView3.setBackground(getDrawable(R.drawable.search));
            TextView textView4 = findViewById(R.id.cart_text);
            textView4.setTextColor(Color.BLACK);
            ImageView imageView1 = findViewById(R.id.cart_icon);
            imageView1.setBackground(getDrawable(R.drawable.cart));
            TextView textView2 = findViewById(R.id.search_text);
            textView2.setTextColor(Color.BLACK);
        });
        sell_layout.setBackgroundResource(backgroundResource);
    }
}
