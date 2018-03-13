package com.codegreeddevelopers.patapotea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 300000000; i++) {

        }
        //direct user to main activity
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
        finish();

    }

}
