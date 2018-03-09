package com.codegreeddevelopers.patapotea;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreeddevelopers.patapotea.R;

import Adapter.Add_Item_PageAdapter;

public class AddItemActivity extends AppCompatActivity {

    ImageView iv_next;
    ImageView iv_done;
    LinearLayout previous,next;
    LinearLayout linear1;
    TextView txt1, txt2, txt3;
    int CURRENTPAGE = 0;
    private ViewPager viewPager;
    private Context context = AddItemActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        linear1 = findViewById(R.id.linear1);
        next = findViewById(R.id.next);
        iv_done = findViewById(R.id.iv_done);
        iv_next = findViewById(R.id.iv_next);
        previous = findViewById(R.id.previous);

        txt1 = findViewById(R.id.text1);
        txt2 = findViewById(R.id.text2);
        txt3 = findViewById(R.id.text3);

        viewPager = findViewById(R.id.viewpager);
        Add_Item_PageAdapter add_item_pageAdpter = new Add_Item_PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(add_item_pageAdpter);
        viewPager.setOffscreenPageLimit(3); // the number of "off screen" pages to keep loaded each side of the current page


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CURRENTPAGE = position;
                setcompletedStates(CURRENTPAGE);
                if (CURRENTPAGE == 0) {
                    previous.setVisibility(View.GONE);
                } else {
                    previous.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextclick();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousclick();
            }
        });
    }


    public void nextclick() {

        if (CURRENTPAGE < 2) {
            CURRENTPAGE++;
            viewPager.setCurrentItem(CURRENTPAGE);
            setcompletedStates(CURRENTPAGE);
            Log.e("CURRENTPAGE", CURRENTPAGE + "");
        }
        if (CURRENTPAGE != 2) {
            iv_done.setVisibility(View.GONE);
            iv_next.setVisibility(View.VISIBLE);
        } else {
            iv_done.setVisibility(View.VISIBLE);
            iv_next.setVisibility(View.GONE);
        }

    }


    public void previousclick() {
        if (CURRENTPAGE < 3 && CURRENTPAGE != 0) {
            CURRENTPAGE--;
            viewPager.setCurrentItem(CURRENTPAGE);
            setcompletedStates(CURRENTPAGE);
            Log.e("CURRENTPAGE", CURRENTPAGE + "");

            iv_done.setVisibility(View.GONE);
            iv_next.setVisibility(View.VISIBLE);
        }

    }

    private void setcompletedStates(int CURRENTPAGE) {
        if (CURRENTPAGE == 0) {
            txt1.setTextColor(ContextCompat.getColor(context, R.color.white));
            txt2.setTextColor(ContextCompat.getColor(context, R.color.green));
            txt3.setTextColor(ContextCompat.getColor(context, R.color.green));


            txt1.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_green_circle));
            txt2.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green_circle));
            txt3.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green_circle));
        }
        if (CURRENTPAGE == 1) {

            txt1.setTextColor(ContextCompat.getColor(context, R.color.white));
            txt2.setTextColor(ContextCompat.getColor(context, R.color.white));
            txt3.setTextColor(ContextCompat.getColor(context, R.color.green));

            txt1.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_green_circle));
            txt2.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_green_circle));
            txt3.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green_circle));
        }
        if (CURRENTPAGE == 2) {

            txt1.setTextColor(ContextCompat.getColor(context, R.color.white));
            txt2.setTextColor(ContextCompat.getColor(context, R.color.white));
            txt3.setTextColor(ContextCompat.getColor(context, R.color.white));

            txt1.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_green_circle));
            txt2.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_green_circle));
            txt3.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_green_circle));
        }
    }
}
