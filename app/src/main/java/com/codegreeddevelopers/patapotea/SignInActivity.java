package com.codegreeddevelopers.patapotea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AppCompatActivity {
    String current_user;
    LinearLayout signinback;
    TextView sign_up,email,password,signin_btn;
    String email_info,password_info,preference_email;
    SweetAlertDialog pDialog;
    SharedPreferences user_preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtain an instance of the SharedPreferences class
        user_preferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        setContentView(R.layout.activity_sign_in);

        //receiving the type of user from StartActivity
        Bundle bundle = getIntent().getExtras();
        current_user=bundle.getString("current_user");

        signinback = findViewById(R.id.signinback);
        sign_up = findViewById(R.id.sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin_btn = findViewById(R.id.signin_btn);

        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignInActivity.this,StartActivity.class);
                startActivity(it);
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserInfo();
            }
        });

        //show signup for normal users
        if (current_user.equals("normal_user")){
            sign_up.setVisibility(View.VISIBLE);

        }

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    public void GetUserInfo(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f158b940"));
        pDialog.setTitleText("Creating Account");
        pDialog.setCancelable(false);
        pDialog.show();
        email_info = email.getText().toString().trim();
        password_info = password.getText().toString().trim();

        CheckUserInfo(email_info,password_info);

    }

    private void CheckUserInfo(final String email, String password) {
        RequestParams params = new RequestParams();

        params.put("email", email);
        params.put("password", password);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.co.ke/patapotea/check_user.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong. Unable to SignIn.")
                        .setCancelText("Cancel")
                        .setConfirmText("Retry")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                GetUserInfo();


                            }
                        })
                        .show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                pDialog.dismissWithAnimation();
                if (responseString.trim().equals("true")){
                    //save the email to preference
                    editor= user_preferences.edit();
                    editor.putString("email",email);
                    editor.apply();

                    Intent it = new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(it);
                    finish();

                }else {
                    new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("Invalid username or password.")
                            .setConfirmText("Retry")
                            .showCancelButton(false)
                            .show();

                }


            }
        });
    }
}
