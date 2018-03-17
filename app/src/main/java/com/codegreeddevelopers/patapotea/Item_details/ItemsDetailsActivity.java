package com.codegreeddevelopers.patapotea.Item_details;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.codegreeddevelopers.patapotea.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class ItemsDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView img_logo, logo_botswana, backArrow, img_share, img_share1, backArrow1, background_img;
    TextView txtheading, textview1, item_number, item_name, date_found, pickup_loc,pickupfee;
    LinearLayout claim_button;
    Context context;
    MapView mapView;
    GoogleMap mMap;
    String data,img;
    SweetAlertDialog fetching_dialog;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 0;
    AlertDialog.Builder popDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        data = getIntent().getStringExtra("item_id");

        //map adding
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        mapFragment.getMapAsync(this);

        //start of loading sweet alert
        fetching_dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        fetching_dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        fetching_dialog.setTitleText("Fetching Data...");
        fetching_dialog.setCancelable(false);
        fetching_dialog.show();

        //request Location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
            return;
        }


        //claiming button
        IconicsImageView iconicsImageView=findViewById(R.id.image_icon);
        claim_button = findViewById(R.id.linear_button);
        claim_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_checkout_pin(data);
            }
        });
        background_img = findViewById(R.id.iv_shop);
        item_name = findViewById(R.id.item_name_display);
        item_number = findViewById(R.id.item_number_display);
        date_found = findViewById(R.id.date_found_display);
        img_logo = findViewById(R.id.img_logo);
        backArrow = findViewById(R.id.backArrow);
        backArrow1 = findViewById(R.id.backArrow1);
        txtheading = findViewById(R.id.txtheading);
        pickup_loc = findViewById(R.id.pickup_loc);

        img_share = findViewById(R.id.img_share);
        img_share1 = findViewById(R.id.img_share1);
        pickupfee=findViewById(R.id.pickup_fee);

        iconicsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_image(img);
            }
        });

        fetch_data_online(data);

        item_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logo_botswana = findViewById(R.id.logo_botswana);
        textview1 = findViewById(R.id.textview1);
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

                    img_share1.setVisibility(View.GONE);
                    backArrow1.setVisibility(View.GONE);

                    //   toolbar.setBackgroundColor(getResources().getColor(R.color.transperent));
                }
            }
        });


    }

    public void fetch_data_online(String item_id) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("item_id", item_id);
        asyncHttpClient.post("http://www.duma.co.ke/patapotea/search_single_item.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                fetching_dialog.dismiss();
                new SweetAlertDialog(ItemsDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Failed to retrieve Data\n Try Again")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ItemsDetailsActivity.super.onBackPressed();
                            }
                        })
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                fetching_dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(responseString);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    textview1.setText(jsonObject.get("item_type").toString());
                    item_name.setText(jsonObject.get("item_name").toString());
                    item_number.setText(jsonObject.get("item_number").toString());
                    txtheading.setText(jsonObject.get("item_type").toString());
                    date_found.setText(jsonObject.get("dateFound").toString());
                    pickup_loc.setText(jsonObject.get("physical_address").toString());
                    pickupfee.setText("Ksh."+jsonObject.get("item_price").toString());
                    img=jsonObject.get("item_image").toString();

                    if (jsonObject.get("loc_lat").toString().equals(null)) {

                    } else {
                        load_marker(jsonObject.get("loc_lat").toString(), jsonObject.get("loc_loc").toString());
                    }
                    //Picasso.with(ItemsDetailsActivity.this).load(R.drawable.item_diaplay_placeholder).placeholder(R.drawable.item_diaplay_placeholder).into(background_img);

                    //estting the image type
                    if (jsonObject.get("item_type").toString().trim().equals("ATM Card")) {
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.credit_card_icon).into(logo_botswana);
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.credit_card_icon).into(img_logo);
                    } else if (jsonObject.get("item_type").toString().trim().equals("Student ID")) {
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.student_id_card).into(logo_botswana);
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.student_id_card).into(img_logo);
                    } else if (jsonObject.get("item_type").toString().trim().equals("Passport")) {
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.passport_icon).into(logo_botswana);
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.passport_icon).into(img_logo);
                    } else if (jsonObject.get("item_type").toString().trim().equals("Visa Card")) {
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.visa_card_icon).into(logo_botswana);
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.visa_card_icon).into(img_logo);
                    } else {
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.national_card).into(logo_botswana);
                        Picasso.with(ItemsDetailsActivity.this).load(R.drawable.national_card).into(img_logo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void get_checkout_pin(String item_id) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data...");
        pDialog.setCancelable(false);
        pDialog.show();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("item_id", item_id);

        httpClient.post("http://www.duma.co.ke/patapotea/get_item_password.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                new SweetAlertDialog(ItemsDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Failed")
                        .show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                pDialog.dismiss();
                //Toast.makeText(ItemsDetailsActivity.this, responseString, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(responseString);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.get("payment_status").toString().equals("NOT")) {
                        new SweetAlertDialog(ItemsDetailsActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setContentText("Please send Your PickUp Fee to +254724487464")
                                .show();
                    } else {
                        new SweetAlertDialog(ItemsDetailsActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setContentText("Your Pick Up Pin is: \n" + jsonObject.get("confirm_code"))
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomGesturesEnabled(true);

    }
    public void load_marker(String lat,String longi){
        LatLng mypickup = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
        mMap.addMarker(new MarkerOptions().position(mypickup).title("My Item PickUp Point"));
        CameraPosition pickup=CameraPosition.builder().target(mypickup).zoom(16).bearing(0).tilt(45).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pickup));
    }

    public void load_image(String img){
        //Toast.makeText(ItemsDetailsActivity.this, img, Toast.LENGTH_SHORT).show();
        ImagePopup imagePopup=new ImagePopup(this);
        imagePopup.setBackground(getResources().getDrawable(R.drawable.item_diaplay_placeholder));
        imagePopup.initiatePopupWithPicasso("http://"+img);
        imagePopup.viewPopup();
    }

}
