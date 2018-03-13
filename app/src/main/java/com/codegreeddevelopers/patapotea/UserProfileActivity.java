package com.codegreeddevelopers.patapotea;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codegreeddevelopers.patapotea.PicupPoint.PickupMain;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    TextView top_name,name,phone,email;
    String display_name,display_email,preference_email,display_phone="";
    Uri profile_url;
    CircleImageView profile;
    ImageView ic_edt_name,ic_edt_phone,ic_edt_email;
    SweetAlertDialog pDialog;
    SharedPreferences user_preferences,pickup_point_preferences;
    SharedPreferences.Editor editor;
    CardView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // obtain an instance of the SharedPreferences class
        user_preferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        pickup_point_preferences = this.getSharedPreferences("PickUpPointInfo", MODE_PRIVATE);

        preference_email = user_preferences.getString("email", null);
        display_phone = user_preferences.getString("phone", null);
        display_name = user_preferences.getString("name", null);
        display_email = preference_email;


        //finding views
        top_name= findViewById(R.id.top_name);
        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        email= findViewById(R.id.email);
        profile=findViewById(R.id.profile);
        ic_edt_name=findViewById(R.id.ic_edt_name);
        ic_edt_phone=findViewById(R.id.ic_edt_phone);
        ic_edt_email=findViewById(R.id.ic_edt_email);
        logout=findViewById(R.id.logout);


//        FetchUserInfo();
        DisplayUserInfo();


        ic_edt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNameDialog();
            }
        });
        ic_edt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEmailDialog();

            }
        });
        ic_edt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePhoneDialog();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
    }

    public void FetchUserInfo(){

        RequestParams params = new RequestParams();
        params.put("email", preference_email);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.co.ke/patapotea/fetch_user_info.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong.")
                        .setCancelText("Cancel")
                        .setConfirmText("Retry")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent it = new Intent(UserProfileActivity.this,PickupMain.class);
                                startActivity(it);
                                finish();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                FetchUserInfo();
                            }
                        })
                        .show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONArray contact_items=new JSONArray(responseString);

                    JSONObject info=contact_items.getJSONObject(0);
                    try {
                        display_name=info.getString("name");
                        display_email=info.getString("email");
                        display_phone=info.getString("phone");

                        DisplayUserInfo();


                    }catch (JSONException e){
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void DisplayUserInfo(){

        if (display_name!=null){
            if (display_name.isEmpty()){
                top_name.setText(R.string.add_name);
                name.setText(R.string.add_name);
                name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                name.setTextSize(15);
            }else{
                top_name.setText(display_name);
                name.setText(display_name);
            }

        }else {
            top_name.setText(R.string.add_name);
            name.setText(R.string.add_name);
            name.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            name.setTextSize(15);

        }
        if (display_phone!=null){
            if(display_phone.isEmpty()){
                phone.setText(R.string.add_phone);
                phone.setTextSize(15);
                phone.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            }else {
                phone.setText(display_phone);
            }

        }else {
            phone.setText(R.string.add_phone);
            phone.setTextSize(15);
            phone.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
        if (display_email.isEmpty()){
            email.setText(R.string.add_eamil);
            email.setTextSize(15);
            email.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }else {
            email.setText(display_email);

        }
//
//
//        if (profile_url != null) {
//            Picasso
//                    .with(getBaseContext())
//                    .load(profile_url)
//                    .transform(new CropCircleTransformation())
//                    .resize(512, 512)
//                    .centerCrop()
//                    .placeholder(R.drawable.avatar)
//                    .into(profile);
//
//        }else {
//            Picasso
//                    .with(getBaseContext())
//                    .load(R.drawable.avatar)
//                    .transform(new CropCircleTransformation())
//                    .resize(512, 512)
//                    .centerCrop()
//                    .into(profile);
//
//            //Else It will display the default dp
//        }

    }
    public void ChangeNameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText edt = new EditText(this);
        edt.setText(name.getText());
        if (name.getText().equals("Add name")){
            edt.setText("");

        }

        dialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Update name</font>"));
        dialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Input your new name</font>"));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setView(edt);
        edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        edt.setSelection(edt.getText().length());

        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_name=edt.getText().toString();
                        if (edt_name.isEmpty()){
                            b.dismiss();
                            new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error")
                                    .setContentText("Username can't be empty!")
                                    .show();
                        }else if(edt_name.equals(display_name)){
                            b.dismiss();
                            new SweetAlertDialog(UserProfileActivity.this)
                                    .setTitleText("No changes made")
                                    .show();
                        }else{
                            pDialog = new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Updating");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            name.setText(edt_name);
                            UpdateName(edt_name);
                            b.dismiss();

                        }

                    }
                });
            }
        });
        b.show();
    }
    public void ChangePhoneDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText edt = new EditText(this);
        edt.setText(phone.getText());
        if (phone.getText().equals("Add mobile number")){
            edt.setText("");

        }
        edt.setInputType(InputType.TYPE_CLASS_PHONE);
        edt.setSelection(edt.getText().length());

        dialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Update name</font>"));
        dialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Input your new phone number</font>"));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setView(edt);

        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_phone=edt.getText().toString();

                        phone.setText(edt_phone);
                        b.dismiss();
                        pDialog = new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Updating");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        UpdatePhone(edt_phone);

                    }
                });
            }
        });
        b.show();
    }
    public void ChangeEmailDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText edt = new EditText(this);
        edt.setText(email.getText());
        if (email.getText().equals("Add email address")){
            edt.setText("");

        }
        edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edt.setSelection(edt.getText().length());

        dialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Update name</font>"));
        dialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Input your new email address</font>"));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setView(edt);

        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_email=edt.getText().toString();
                        UpdateEmail(edt_email);
                        b.dismiss();
                        pDialog = new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Updating");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        email.setText(edt_email);


                    }
                });
            }
        });
        b.show();
    }
    public void UpdateName(final String new_name){
        RequestParams params = new RequestParams();
        params.put("current_email", preference_email);
        params.put("name", new_name);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.co.ke/patapotea/update_user_info.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong.")
                        .setCancelText("Cancel")
                        .setConfirmText("Retry")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                UpdateName(new_name);
                            }
                        })
                        .show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("Name successfully updated.")
                        .setConfirmText("Ok")
                        .show();


            }
        });

    }
    public void UpdatePhone(final String new_phone){
        RequestParams params = new RequestParams();
        params.put("current_email", preference_email);
        params.put("phone", new_phone);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.co.ke/patapotea/update_user_info.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong.")
                        .setCancelText("Cancel")
                        .setConfirmText("Retry")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                UpdatePhone(new_phone);
                            }
                        })
                        .show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("Phone successfully updated.")
                        .setConfirmText("Ok")
                        .show();


            }
        });

    }
    public void UpdateEmail(final String new_email){
        RequestParams params = new RequestParams();
        params.put("current_email", preference_email);
        params.put("email", new_email);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.co.ke/patapotea/update_user_info.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Something went wrong.")
                        .setCancelText("Cancel")
                        .setConfirmText("Retry")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                UpdateEmail(new_email);
                            }
                        })
                        .show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //update email to user_preferences
                editor= user_preferences.edit();
                editor.putString("email",new_email);
                editor.apply();

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("Email successfully updated.")
                        .setConfirmText("Ok")
                        .show();


            }
        });

    }
    public void LogOut(){
        //clear the data from items_preference and restart app
        user_preferences.edit().clear().apply();
        RestartApp();
    }

    public void RestartApp(){
        Intent intent=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

