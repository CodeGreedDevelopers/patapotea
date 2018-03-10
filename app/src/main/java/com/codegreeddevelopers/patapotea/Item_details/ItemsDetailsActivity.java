package com.codegreeddevelopers.patapotea.Item_details;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codegreeddevelopers.patapotea.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public class ItemsDetailsActivity extends AppCompatActivity {

    ImageView img_logo, logo_botswana, backArrow, img_star, img_share, img_star1, img_share1, backArrow1;
    TextView txtheading, textview1;
    Context context;
    MapView mapView;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        img_logo =  findViewById(R.id.img_logo);
        backArrow =  findViewById(R.id.backArrow);
        backArrow1 =  findViewById(R.id.backArrow1);
        txtheading =  findViewById(R.id.txtheading);

        img_star =  findViewById(R.id.img_star);
        img_share =  findViewById(R.id.img_share);
        img_star1 =  findViewById(R.id.img_star1);
        img_share1 =  findViewById(R.id.img_share1);



        logo_botswana =  findViewById(R.id.logo_botswana);
        textview1 =  findViewById(R.id.textview1);
        txtheading.setText("National ID");
        //back arrow pressed
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsDetailsActivity.super.onBackPressed();
            }
        });
        backArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsDetailsActivity.super.onBackPressed();
            }
        });

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.mainappbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {

                    //is collapsed
                    logo_botswana.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    txtheading.setVisibility(View.VISIBLE);
                    txtheading.setTextColor(Color.parseColor("#4d4d4d"));
                    img_star1.setVisibility(View.VISIBLE);
                    img_share1.setVisibility(View.VISIBLE);
                    img_logo.setVisibility(View.VISIBLE);
                    backArrow1.setVisibility(View.VISIBLE);
                    isShow = true;
//                    toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else if (isShow) {
                    isShow = false;

                    //is not collapsed
                    logo_botswana.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    txtheading.setVisibility(View.GONE);
                    img_logo.setVisibility(View.GONE);

                    img_star1.setVisibility(View.GONE);
                    img_share1.setVisibility(View.GONE);
                    backArrow1.setVisibility(View.GONE);

                    //   toolbar.setBackgroundColor(getResources().getColor(R.color.transperent));
                }
            }
        });


    }
}
