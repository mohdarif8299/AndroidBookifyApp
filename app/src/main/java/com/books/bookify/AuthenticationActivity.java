package com.books.bookify;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.books.bookify.user.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthenticationActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    MediaType MEDIA_TYPE = MediaType.parse("application/json");
    OkHttpClient client;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key1),Context.MODE_PRIVATE);
        if (sharedPref.getString("username",null)!=null) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        client = new OkHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = this.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        SignInButton signInButton = findViewById(R.id.signin_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        Typeface face = ResourcesCompat.getFont(this, R.font.didact_gothic);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Login via Google");
        textView.setTextSize(12);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(face);
        login();
        signInButton.setOnClickListener(v -> {
            signIn();
        });
        LinearLayout linearLayout = findViewById(R.id.signup);
        linearLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrationActivity.class));
        });
        linearLayout.setBackgroundResource(backgroundResource);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE SIGN IN REPORT", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) return;
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "" + account.getDisplayName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "" + account.getEmail(), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", account.getEmail());
        editor.putString("displayname", account.getDisplayName());
        editor.commit();
    }

    private void login() {
        Button login = findViewById(R.id.login);
        login.setEnabled(false);
        EditText username = findViewById(R.id.username);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    username.requestFocus();
                    login.setEnabled(false);
                    login.setBackgroundColor(getColor(R.color.unselected_color));
                }
            }
        });
        EditText password = findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5 && username.getText().toString().length() != 0) {
                    login.setEnabled(true);
                    login.setBackgroundColor(getColor(R.color.colorPrimary));
                } else {
                    password.requestFocus();
                    login.setEnabled(false);
                    login.setBackgroundColor(getColor(R.color.unselected_color));
                }
            }
        });
        login.setOnClickListener(v -> {
                try {
                    JSONObject postdata = new JSONObject();
                    postdata.put("username",username.getText().toString());
                    postdata.put("password",password.getText().toString());
                    Log.d("Json",""+postdata.toString());
                    String response = post("https://enigmatic-castle-41717.herokuapp.com/login",postdata);
                    Log.d("RegisterResponse",response+"");
                if (response.contains("Invalid Username")){
                    Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show();
                }
                else if (response.contains("Invalid Password")) {
                    Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONObject jsonObject = new JSONObject(response);
                    Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.login_loader);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.show();
                    UserDetails userDetails = new UserDetails(jsonObject.getString("name"), jsonObject.getString("username"), jsonObject.getString("password"), jsonObject.getString("number"));
                    SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key1), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", userDetails.getName());
                    editor.putString("username", userDetails.getUsername());
                    editor.commit();
                    Log.d("Name", "" + jsonObject.getString("name"));
                    dialog.dismiss();
                    startActivity(new Intent(this, MainActivity.class));
                }
            } catch (IOException | JSONException e) {
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
