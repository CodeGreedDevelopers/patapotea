package com.codegreeddevelopers.patapotea;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class SearchAccount extends AppCompatActivity {

    Button search_btn;
    EditText reset_identity;
    String st_user_identity;
    public String current_user;
    AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //receiving the type of user from sign in activity
        final Bundle bundle = getIntent().getExtras();
        current_user=bundle.getString("current_user");

        //initializing async httpclient
        httpClient = new AsyncHttpClient();

        search_btn=findViewById(R.id.reset_search_btn);
        reset_identity=findViewById(R.id.reset_user_id);

        //Testing bundles
        Toast.makeText(this, "Current User: "+current_user, Toast.LENGTH_SHORT).show();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reset_identity.getText().toString().isEmpty()){
                    reset_identity.setError("Please Enter Your Email");
                }else{
                    st_user_identity=reset_identity.getText().toString();
                }


            }
        });

    }
    public void submit_data_online(String email,String userType){
        RequestParams params = new RequestParams();
        params.put("useremail",email);
        params.put("usertype",userType);

        httpClient.post("http://www.duma.co.ke/patapotea/resend_password.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }


}
