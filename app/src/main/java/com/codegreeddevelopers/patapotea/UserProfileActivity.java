package com.codegreeddevelopers.patapotea;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    TextView top_name,name,phone,email;
    String display_name,display_email,preference_email,display_phone="",user_type,result,profile_url,cached_profile_url;
    CircleImageView profile;
    ImageView ic_edt_name,ic_edt_phone,ic_edt_email;
    SharedPreferences user_preferences,pickup_point_preferences;
    SweetAlertDialog pDialog,sDialog;
    SharedPreferences.Editor editor;
    CardView logout;
    AlertDialog.Builder popDialog;
    AVLoadingIndicatorView avi;
    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // obtain an instance of the SharedPreferences class
        user_preferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        pickup_point_preferences = this.getSharedPreferences("PickUpPointInfo", MODE_PRIVATE);

        if (user_preferences.getString("email", null)==null){
            user_type="pickup_point";
        }else {
            user_type="normal_user";
        }


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
        avi=findViewById(R.id.avi);

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
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_storage_permission();

            }
        });
    }

    public void DisplayUserInfo(){

        if (user_type.equals("normal_user")){
            preference_email = user_preferences.getString("email", null);
            display_phone = user_preferences.getString("phone", null);
            display_name = user_preferences.getString("name", null);
            profile_url = user_preferences.getString("profile_url", null);
        }else {
            preference_email = pickup_point_preferences.getString("email", null);
            display_phone = pickup_point_preferences.getString("phone", null);
            display_name = pickup_point_preferences.getString("name", null);
            profile_url = user_preferences.getString("profile_url", null);
        }

        display_email = preference_email;

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
        if(user_type=="normal_user"){
            cached_profile_url = user_preferences.getString("cached_profile_url", null);
        }else{
            cached_profile_url = pickup_point_preferences.getString("cached_profile_url", null);
        }


        if (cached_profile_url != null){
            profile.setImageURI(Uri.parse(cached_profile_url));
        }else{
            if (profile_url != null) {
                if (profile_url.isEmpty()){
                    Picasso
                            .with(getBaseContext())
                            .load(R.drawable.dp_placeholder)
                            .resize(512, 512)
                            .centerCrop()
                            .into(profile);
                }else{
                    Picasso
                            .with(getBaseContext())
                            .load(profile_url)
                            .resize(512, 512)
                            .placeholder(R.drawable.dp_placeholder)
                            .centerCrop()
                            .into(profile);
                }


            }else {
                Picasso
                        .with(getBaseContext())
                        .load(R.drawable.dp_placeholder)
                        .resize(512, 512)
                        .centerCrop()
                        .into(profile);
            }

        }

    }
    public void ChangeNameDialog() {
//        new LovelyTextInputDialog(this)
//                .setTopColorRes(R.color.colorPrimary)
//                .setTitle("Update your name")
//                .setMessage("Please input your new name")
//                .setIcon(R.drawable.ic_usr_dp)
//                .setInputType(InputType.TYPE_CLASS_TEXT)
//                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
//                    @Override
//                    public void onTextInputConfirmed(String text) {
//                        Toast.makeText(UserProfileActivity.this, text, Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .show();
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


                    }
                });
            }
        });
        b.show();
    }
    public void UpdateName(final String new_name){
        RequestParams params = new RequestParams();
        params.put("current_email", preference_email);
        params.put("user_type", user_type);
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

                if (user_type.equals("normal_user")){
                    //update name to pickup_point_preferences
                    editor= user_preferences.edit();
                    editor.putString("name",new_name);
                    editor.apply();
                }else {
                    //update name to user_preferences
                    editor= pickup_point_preferences.edit();
                    editor.putString("name",new_name);
                    editor.apply();
                }

                pDialog.dismissWithAnimation();
                DisplayUserInfo();
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
        params.put("user_type", user_type);
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
                //update name to pickup_point_preferences
                if (user_type.equals("normal_user")){
                    //update name to user_preferences
                    editor= user_preferences.edit();
                    editor.putString("phone",new_phone);
                    editor.apply();
                }else {
                    //update name to user_preferences
                    editor= pickup_point_preferences.edit();
                    editor.putString("phone",new_phone);
                    editor.apply();
                }

                pDialog.dismissWithAnimation();
                DisplayUserInfo();
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
        params.put("user_type", user_type);
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
                //update name to pickup_point_preferences
                if (user_type.equals("normal_user")){
                    //update name to user_preferences
                    editor= user_preferences.edit();
                    editor.putString("email",new_email);
                    editor.apply();
                }else {
                    //update name to user_preferences
                    editor= pickup_point_preferences.edit();
                    editor.putString("email",new_email);
                    editor.apply();
                }


                pDialog.dismissWithAnimation();
                DisplayUserInfo();
                new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("Email successfully updated.")
                        .setConfirmText("Ok")
                        .show();


            }
        });

    }
    void openImageChooser() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropMenuCropButtonTitle("Done")
                .setAspectRatio(1, 1)
                .start(this);
    }
    private void check_storage_permission(){
        if (ActivityCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},100);

            }

        }else {
            openImageChooser();
        }

    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        UpdateProfileImage(UserProfileActivity.this);
        return result;
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                new File(getRealPathFromURI(resultUri));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();
            }
        }

    }
    public void UpdateProfileImage(final Context context){
        avi.smoothToShow();
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            params.put("profile_image", new File(result));
            params.put("current_email", preference_email);
            params.put("user_type", user_type);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post("http://www.duma.co.ke/patapotea/update_user_info.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                avi.smoothToHide();
                profile.setImageURI(resultUri);

                try {
                    //convert the byte response to string so as to get profile url
                    String str = new String(bytes, "UTF-8");
                    if (user_type.equals("normal_user")){
                        cached_profile_url=resultUri.toString();
                        //update profile url to pickup_point_preferences
                        editor= user_preferences.edit();
                        editor.putString("profile_url",str);
                        editor.putString("cached_profile_url",cached_profile_url);
                        editor.apply();
                    }else {
                        cached_profile_url=resultUri.toString();
                        //update profile url to user_preferences
                        editor= pickup_point_preferences.edit();
                        editor.putString("profile_url",str);
                        editor.putString("cached_profile_url",cached_profile_url);
                        editor.apply();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                avi.smoothToHide();
                sDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                sDialog.setTitleText("Error!");
                sDialog.setContentText("Profile image was not updated. Do you want to retry again?");
                sDialog.setCancelText("Cancel");
                sDialog.setConfirmText("Retry");
                sDialog.showCancelButton(true);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sDialog.dismissWithAnimation();
                        UpdateProfileImage(context);

                    }
                });
                sDialog.show();

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openImageChooser();

        }else {
            popDialog=new AlertDialog.Builder(this);
            popDialog.setTitle("Permission")
                    .setMessage(Html.fromHtml("<font color='#000000'>Please grant the storage permission for the normal working of the app.</font>"))
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            check_storage_permission();

                        }
                    });
            popDialog.create().show();



        }
    }
    public void LogOut(){
        //clear the data from items_preference and restart app
        if (user_type.equals("normal_user")){
            user_preferences.edit().clear().apply();
        }else{
            pickup_point_preferences.edit().clear().apply();
        }

        RestartApp();
    }

    public void RestartApp(){
        Intent intent=getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

