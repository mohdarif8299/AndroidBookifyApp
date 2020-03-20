package com.books.bookify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.books.bookify.user.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

public class RegistrationActivity extends AppCompatActivity {
    private OkHttpClient client;
    MediaType MEDIA_TYPE = MediaType.parse("application/json");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
       client = new OkHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Button signup = findViewById(R.id.signup);
        signup.setEnabled(false);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText cpassword = findViewById(R.id.c_password);
        EditText name = findViewById(R.id.name);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0 || name.getText().length()==0 || password.getText().length()==0|| cpassword.getText().length()==0) {
                    signup.setEnabled(false);
                }
                else {
                    signup.setEnabled(true);
                    signup.setBackgroundResource(R.color.colorPrimary);
                }
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0 || name.getText().length()==0 || password.getText().length()==0|| cpassword.getText().length()==0) {
                    signup.setEnabled(false);
                }
                else {
                    signup.setEnabled(true);
                    signup.setBackgroundResource(R.color.colorPrimary);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()<6 || s.length()==0 || name.getText().length()==0 || cpassword.getText().length()==0) {
                    signup.setBackgroundResource(R.color.unselected_color);
                     }
                else {
                    signup.setEnabled(true);
                    signup.setBackgroundResource(R.color.colorPrimary);
                }
            }
        });
        cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        signup.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.login_loader);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
            try {
                JSONObject postdata = new JSONObject();
                    postdata.put("name",name.getText().toString());
                    postdata.put("username",username.getText().toString());
                    postdata.put("password",password.getText().toString());
                    Log.d("Json",""+postdata.toString());
                    String response = post("https://enigmatic-castle-41717.herokuapp.com/register",postdata);
                    Log.d("RegisterResponse",response+"");
                    dialog.dismiss();
                    onBackPressed();
            } catch (IOException |JSONException e) {
                e.printStackTrace();
            }
        });
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
