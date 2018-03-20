package com.codegreeddevelopers.patapotea;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;


import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;


public class SignUpActivity extends AppCompatActivity {
    LinearLayout signupback;
    CheckBox checkBox;
    TextView email,password,fullname,confirm_password,signup,terms_text;
    String email_info,password_info,fullname_info,confirm_password_info;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupback = findViewById(R.id.signupback);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullname = findViewById(R.id.fullname);
        confirm_password = findViewById(R.id.confirm_password);
        signup = findViewById(R.id.signup);
        terms_text=findViewById(R.id.text_terms);
        checkBox=findViewById(R.id.terms_check);

        terms_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LovelyInfoDialog(SignUpActivity.this)
                        .setTopColorRes(R.color.colorPrimary)
                        .setTopTitle("Privacy, Terms & Conditions")
                        .setIcon(R.drawable.ic_info)
                        .setTitle(R.string.info_title)
                        //.configureMessageView(message->message.setMovementMethod(new ScrollingMovementMethod()))
                        .setMessage(R.string.info_message)
                        .show();
            }
        });

        signupback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignUpActivity.this,SignInActivity.class);
                it.putExtra("current_user", "normal_user");
                startActivity(it);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    GetUserInfo();
                }

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
        fullname_info = fullname.getText().toString().trim();

        UploadUserInfo(fullname_info,email_info,password_info);

    }

    public void UploadUserInfo(String name,String email,String password){
        RequestParams params = new RequestParams();

        params.put("name", name);
        params.put("email", email);
        params.put("password", password);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.co.ke/patapotea/register_user.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                pDialog.dismissWithAnimation();

                new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("Account successfully created. You can now Login")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                Intent it = new Intent(SignUpActivity.this,SignInActivity.class);
                                it.putExtra("current_user", "normal_user");
                                startActivity(it);
                                finish();

                            }
                        })
                        .show();


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong. Unable to create account.")
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
        });

    }
    public boolean validate() {
        boolean valid = true;

        String _name = fullname.getText().toString();
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        String _confirm_pass=confirm_password.getText().toString();

        if (_name.isEmpty() ||_name.length() < 3) {
            fullname.setError("at least 3 characters");
            valid = false;
        } else {
            fullname.setError(null);
        }
        if (checkBox.isChecked()){

        }else{
            Toast.makeText(this, "Please Accept Terms & Conditions", Toast.LENGTH_SHORT).show();
            valid=false;
        }

        if (_password.equals(_confirm_pass)){
            confirm_password.setError(null);
        }else{
            confirm_password.setError("Passwords Do Not Match");
            valid=false;
        }

        if (_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (_password.isEmpty() || _password.length() < 4 || _password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
