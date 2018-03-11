package com.codegreeddevelopers.patapotea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {
    LinearLayout get_pickup_point,get_user;
    SharedPreferences user_preferences;
    SharedPreferences.Editor editor;
    String preference_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // obtain an instance of the SharedPreferences class
        user_preferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        get_user=findViewById(R.id.get_user);
        get_pickup_point=findViewById(R.id.get_pickup_point);


        get_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preference_email = user_preferences.getString("email", null);
                if (preference_email!=null){
                    Intent intent = new Intent(StartActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                    intent.putExtra("current_user", "normal_user");
                    startActivity(intent);
                }


            }
        });

        get_pickup_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(StartActivity.this,SignInActivity.class);
                intent.putExtra("current_user", "pickup_point");
                startActivity(intent);


            }
        });
    }
}
