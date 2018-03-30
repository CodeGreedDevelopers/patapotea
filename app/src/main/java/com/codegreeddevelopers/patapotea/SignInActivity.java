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

import com.codegreeddevelopers.patapotea.PicupPoint.PickupMain;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AppCompatActivity {
    String current_user,db_phone,db_id,db_name,db_response,db_profile_url;
    LinearLayout signinback;
    TextView sign_up,email,password,signin_btn;
    String email_info,password_info,preference_email,db_email;
    SweetAlertDialog pDialog;
    SharedPreferences user_preferences,pickup_point_preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // obtain an instance of the SharedPreferences class
        user_preferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        pickup_point_preferences = this.getSharedPreferences("PickUpPointInfo", MODE_PRIVATE);

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
        pDialog.setTitleText("Signing In...");
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
        params.put("user_type",current_user);

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
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                pDialog.dismissWithAnimation();


                try {
                    JSONArray user_info=new JSONArray(responseString);

                    JSONObject info=user_info.getJSONObject(1);
                    JSONObject r_info=user_info.getJSONObject(0);
                    try {

                        if (current_user.equals("pickup_point")){
                            db_id=info.getString("id");

                        }
                        db_phone=info.getString("phone");
                        db_name=info.getString("name");
                        db_email=info.getString("email");
                        db_response=r_info.getString("response");
                        db_profile_url=info.getString("profile_image");

                        if (db_response.equals("true")){
                            if (current_user.equals("normal_user")){
                                editor= user_preferences.edit();
                                editor.putString("email",db_email);
                                editor.putString("phone",db_phone);
                                editor.putString("name",db_name);
                                editor.putString("profile_url",db_profile_url);
                                editor.apply();

                                //clear any pickup point preferences that exist
                                pickup_point_preferences.edit().clear().apply();
                                Intent it = new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(it);
                                finish();
                            }else{
                                editor= pickup_point_preferences.edit();
                                editor.putString("id",db_id);
                                editor.putString("email",db_email);
                                editor.putString("phone",db_phone);
                                editor.putString("name",db_name);
                                editor.putString("profile_url",db_profile_url);
                                editor.apply();

                                //clear any normal user preferences that exist
                                user_preferences.edit().clear().apply();
                                Intent it = new Intent(SignInActivity.this,PickupMain.class);
                                startActivity(it);
                                finish();
                            }
                        }else {
                            new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Invalid username or password.")
                                    .setConfirmText("Ok")
                                    .showCancelButton(false)
                                    .show();

                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
