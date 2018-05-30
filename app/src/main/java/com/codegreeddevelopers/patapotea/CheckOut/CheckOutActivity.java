package com.codegreeddevelopers.patapotea.CheckOut;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreeddevelopers.patapotea.Item_details.ItemsDetailsActivity;
import com.codegreeddevelopers.patapotea.MainActivity;
import com.codegreeddevelopers.patapotea.R;
import com.codegreeddevelopers.patapotea.SignInActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by FakeJoker on 11/03/2018.
 */

public class CheckOutActivity extends AppCompatActivity {
    ImageView img_logo, logo_botswana, backArrow, img_share,img_share1, backArrow1,background_img;
    TextView txtheading, textview1,item_number,item_name,date_found,payment_status;
    LinearLayout checkout_button;
    String data;
    SweetAlertDialog fetching_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_item);

        data= getIntent().getStringExtra("item_id");

        //start of loading sweet alert
        fetching_dialog= new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        fetching_dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        fetching_dialog.setTitleText("Fetching Data...");
        fetching_dialog.setCancelable(false);
        fetching_dialog.show();

        //claiming button
        checkout_button=findViewById(R.id.linear_button);
        checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_checkout();

            }
        });
        background_img=findViewById(R.id.piv_shop);
        item_name=findViewById(R.id.pitem_name_display);
        item_number=findViewById(R.id.pitem_number_display);
        date_found=findViewById(R.id.pdate_found_display);
        img_logo =  findViewById(R.id.pimg_logo);
        backArrow =  findViewById(R.id.pbackArrow);
        backArrow1 =  findViewById(R.id.pbackArrow1);
        txtheading =  findViewById(R.id.ptxtheading);
        payment_status = findViewById(R.id.payment_status);

        fetch_data_online(data);

        img_share =  findViewById(R.id.pimg_share);
        img_share1 =  findViewById(R.id.pimg_share1);

        logo_botswana =  findViewById(R.id.plogo_botswana);
        textview1 =  findViewById(R.id.ptextview1);
        txtheading.setText("National ID");
        //back arrow pressed
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutActivity.super.onBackPressed();
            }
        });
        backArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOutActivity.super.onBackPressed();
            }
        });

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.pmainappbar);
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
                    txtheading.setTextColor(Color.parseColor("#4d4d4d"));
                    img_share1.setVisibility(View.VISIBLE);
                    img_logo.setVisibility(View.VISIBLE);
                    backArrow1.setVisibility(View.VISIBLE);
                    isShow = true;
                    logo_botswana.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    txtheading.setVisibility(View.VISIBLE);
//                    toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else if (isShow) {
                    isShow = false;

                    //is not collapsed
                    logo_botswana.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    txtheading.setVisibility(View.GONE);
                    img_logo.setVisibility(View.GONE);

                    img_share1.setVisibility(View.GONE);
                    backArrow1.setVisibility(View.GONE);

                    //   toolbar.setBackgroundColor(getResources().getColor(R.color.transperent));
                }
            }
        });
    }

    public void fetch_data_online(String item_id){
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("item_id",item_id);
        asyncHttpClient.post("http://www.duma.co.ke/patapotea/search_single_item.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                fetching_dialog.dismiss();
                new SweetAlertDialog(CheckOutActivity.this,SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Failed")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                CheckOutActivity.super.onBackPressed();
                            }
                        })
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                fetching_dialog.dismiss();
                try {
                    JSONArray jsonArray=new JSONArray(responseString);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    textview1.setText(jsonObject.get("item_type").toString());
                    item_name.setText(jsonObject.get("item_name").toString());
                    item_number.setText(jsonObject.get("item_number").toString());
                    txtheading.setText(jsonObject.get("item_type").toString());
                    payment_status.setText(jsonObject.get("payment_status").toString());
                    date_found.setText(jsonObject.get("dateFound").toString());
                    //Picasso.with(ItemsDetailsActivity.this).load(R.drawable.item_diaplay_placeholder).placeholder(R.drawable.item_diaplay_placeholder).into(background_img);

                    //estting the image type
                    if (jsonObject.get("item_type").toString().trim().equals("ATM Card")){
                        Picasso.with(CheckOutActivity.this).load(R.drawable.credit_card_icon).into(logo_botswana);
                        Picasso.with(CheckOutActivity.this).load(R.drawable.credit_card_icon).into(img_logo);
                    }else if(jsonObject.get("item_type").toString().trim().equals("Student ID")){
                        Picasso.with(CheckOutActivity.this).load(R.drawable.student_id_card).into(logo_botswana);
                        Picasso.with(CheckOutActivity.this).load(R.drawable.student_id_card).into(img_logo);
                    }else if(jsonObject.get("item_type").toString().trim().equals("Passport")){
                        Picasso.with(CheckOutActivity.this).load(R.drawable.passport_icon).into(logo_botswana);
                        Picasso.with(CheckOutActivity.this).load(R.drawable.passport_icon).into(img_logo);
                    }else if (jsonObject.get("item_type").toString().trim().equals("Visa Card")){
                        Picasso.with(CheckOutActivity.this).load(R.drawable.visa_card_icon).into(logo_botswana);
                        Picasso.with(CheckOutActivity.this).load(R.drawable.visa_card_icon).into(img_logo);
                    }else{
                        Picasso.with(CheckOutActivity.this).load(R.drawable.national_card).into(logo_botswana);
                        Picasso.with(CheckOutActivity.this).load(R.drawable.national_card).into(img_logo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void confirm_checkout(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle("Item Check Out")
                .setMessage("Please Enter the Checkout Pin")
                .setIcon(R.drawable.ic_info)
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        get_item_data(text);
                    }
                })
                .show();
    }
    public void get_item_data(final String conf_code){
        fetching_dialog.show();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("conf_code",conf_code);
        params.put("item_id",data);

        asyncHttpClient.post("http://www.duma.co.ke/patapotea/confirm_checkout.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                fetching_dialog.dismiss();
                new SweetAlertDialog(CheckOutActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong.")
                        .setCancelText("Cancel")
                        .setConfirmText("Retry")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                get_item_data(conf_code);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                fetching_dialog.dismiss();
                Toast.makeText(CheckOutActivity.this, responseString, Toast.LENGTH_SHORT).show();
                if (responseString.trim().equals("true")){
                    new SweetAlertDialog(CheckOutActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("CheckOut Successfull")
                            .show();
                }else{
                    new SweetAlertDialog(CheckOutActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("CheckOut Failed \n Wrong Checkout Pin")
                            .show();
                }
            }
        });

    }
}
