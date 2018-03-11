package com.codegreeddevelopers.patapotea;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    TextView top_name,name,phone,email;
    String display_name,display_email;
    String display_phone="";
    Uri profile_url;
    CircleImageView profile;
    ImageView ic_edt_name,ic_edt_phone,ic_edt_email;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //finding views
        top_name= findViewById(R.id.top_name);
        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        email= findViewById(R.id.email);
        profile=findViewById(R.id.profile);
        ic_edt_name=findViewById(R.id.ic_edt_name);
        ic_edt_phone=findViewById(R.id.ic_edt_phone);
        ic_edt_email=findViewById(R.id.ic_edt_email);

        ic_edt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChangeNameDialog();
            }
        });
        ic_edt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChangeEmailDialog();

            }
        });
        ic_edt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChangePhoneDialog();

            }
        });
    }
}
