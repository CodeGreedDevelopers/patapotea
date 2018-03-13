package com.codegreeddevelopers.patapotea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreeddevelopers.patapotea.PicupPoint.PickupMain;
import com.codegreeddevelopers.patapotea.R;

import Adapter.Add_Item_PageAdapter;
import Fragment.Fragment_Add_Item1;
import Fragment.Fragment_Add_Item2;
import Fragment.Fragment_Add_Item3;

public class AddItemActivity extends AppCompatActivity {

    ImageView iv_next;
    ImageView iv_done;
    LinearLayout previous,next;
    LinearLayout linear1;
    TextView txt1, txt2, txt3;
    int CURRENTPAGE = 0;
    private ViewPager viewPager;
    private Context context = AddItemActivity.this;

    SharedPreferences preferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("AddItem", MODE_PRIVATE);

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

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            if (CURRENTPAGE==0){
                //get the saved selected item from preference
                String item_name=preferences.getString("item_name",null);
                String item_number=preferences.getString("item_number",null);

                //check if values of edit texts are empty
                if (item_number.isEmpty()&&item_name.isEmpty()){
                    Fragment_Add_Item1.ChangeBackgroundDrawable("item_number");
                    Fragment_Add_Item1.ChangeBackgroundDrawable("item_name");
                }else if (item_number.isEmpty()){
                    Fragment_Add_Item1.ChangeBackgroundDrawable("item_number");
                }else if (item_name.isEmpty()){
                    Fragment_Add_Item1.ChangeBackgroundDrawable("item_name");
                }else {
                    Fragment_Add_Item1.ChangeBackgroundDrawable("");
                    CURRENTPAGE++;
                    viewPager.setCurrentItem(CURRENTPAGE);
                    setcompletedStates(CURRENTPAGE);
                    Log.e("CURRENTPAGE", CURRENTPAGE + "");

                }
            }else{
                //get the saved selected item from preference
                String founder_name=preferences.getString("founder_name",null);
                String founder_phone=preferences.getString("founder_phone",null);
                String founder_id=preferences.getString("founder_id",null);

                //check if values of edit texts are empty
                if (founder_phone.isEmpty()&&founder_name.isEmpty()&&founder_id.isEmpty()){
                    Fragment_Add_Item2.ChangeBackgroundDrawable("founder_name");
                    Fragment_Add_Item2.ChangeBackgroundDrawable("founder_phone");
                    Fragment_Add_Item2.ChangeBackgroundDrawable("founder_id");
                }else if (founder_phone.isEmpty()){
                    Fragment_Add_Item2.ChangeBackgroundDrawable("founder_name");
                }else if (founder_name.isEmpty()){
                    Fragment_Add_Item2.ChangeBackgroundDrawable("founder_phone");
                }else {
                    Fragment_Add_Item2.ChangeBackgroundDrawable("");
                    CURRENTPAGE++;
                    viewPager.setCurrentItem(CURRENTPAGE);
                    setcompletedStates(CURRENTPAGE);
                    Log.e("CURRENTPAGE", CURRENTPAGE + "");

                }

            }


        }
        if (CURRENTPAGE != 2) {
            iv_done.setVisibility(View.GONE);
            iv_next.setVisibility(View.VISIBLE);
        } else {

            iv_done.setVisibility(View.VISIBLE);
            iv_next.setVisibility(View.GONE);
            iv_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment_Add_Item3.GetItems(AddItemActivity.this);
                }
            });
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
