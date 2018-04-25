package com.codegreeddevelopers.patapotea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        for (int i = 0; i < 300000000; i++) {

        }
        //direct user to main activity
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
        finish();

    }

}
