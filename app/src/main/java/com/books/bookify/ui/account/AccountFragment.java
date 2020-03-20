package com.books.bookify.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.books.bookify.AuthenticationActivity;
import com.books.bookify.ManageAccount;
import com.books.bookify.R;
import com.books.bookify.user.UserUploadBooks;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;
    CircleImageView imgProfile;
    SharedPreferences sharedPref;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_account, container, false);
        imgProfile = root.findViewById(R.id.image);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        LinearLayout logout = root.findViewById(R.id.logout);
        logout.setBackgroundResource(backgroundResource);
        logout.setOnClickListener(v -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key1),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(context, AuthenticationActivity.class));
            });
        });
        LinearLayout manageAccount = root.findViewById(R.id.manage_account);
        manageAccount.setBackgroundResource(backgroundResource);
        manageAccount.setOnClickListener(v->{
            startActivity(new Intent(context, ManageAccount.class));
        });
        LinearLayout userUploads = root.findViewById(R.id.user_uploads);
        userUploads.setBackgroundResource(backgroundResource);
        userUploads.setOnClickListener(v->{
            startActivity(new Intent(context, UserUploadBooks.class));
        });
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
