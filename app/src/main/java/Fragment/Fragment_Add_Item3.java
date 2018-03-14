package Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.codegreeddevelopers.patapotea.PicupPoint.PickupMain;
import com.codegreeddevelopers.patapotea.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Fragment_Add_Item3 extends android.support.v4.app.Fragment {
    public static SharedPreferences items_preferences,pickup_point_preferences;
    public static SharedPreferences.Editor editor;
    private View view;
    public static String item_type,item_number,item_name,founder_name,founder_phone,founder_id,result, pickup_location_preference_id;
    ImageView item_image;
    public static SweetAlertDialog pDialog,sDialog;
    AlertDialog.Builder popDialog;
    public static Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        activity=getActivity();
        view = inflater.inflate(R.layout.fragment_add_item3, container, false);

        // obtain an instance of the SharedPreferences class
        items_preferences = this.getActivity().getSharedPreferences("AddItem", MODE_PRIVATE);
        pickup_point_preferences = this.getActivity().getSharedPreferences("PickUpPointInfo", MODE_PRIVATE);

        item_image=view.findViewById(R.id.item_image);

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_storage_permission();


            }
        });

        return view;
    }
    //get information from the previous fragments
    public static void GetItems(Context context){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#f158b940"));
        pDialog.setTitleText("Saving...");
        pDialog.setCancelable(false);
        pDialog.show();
        item_type = items_preferences.getString("item_type", null);
        item_number = items_preferences.getString("item_number", null);
        item_name = items_preferences.getString("item_name", null);
        founder_name = items_preferences.getString("founder_name", null);
        founder_phone = items_preferences.getString("founder_phone", null);
        founder_id = items_preferences.getString("founder_id", null);
        pickup_location_preference_id = pickup_point_preferences.getString("id", null);
        check_internet_conn(context);

    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File imageFile = new File(getRealPathFromURI(resultUri));

                //Display the image before uploading
                item_image.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = this.getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    void openImageChooser() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropMenuCropButtonTitle("Done")
                .start(getContext(), this);
    }
    public static void UploadItems(final Context context){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            params.put("item_image", new File(result));
            params.put("item_name", item_name);
            params.put("item_number", item_number);
            params.put("item_type", item_type);
            params.put("founder_name", founder_name);
            params.put("founder_id", founder_id);
            params.put("founder_phone", founder_phone);
            params.put("pickup_location_id", pickup_location_preference_id);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post("http://www.duma.co.ke/patapotea/items_data_setter.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                pDialog.dismissWithAnimation();

                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("The details were successfully saved")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                //clear the data from items_preference and redirect to main activity
                                items_preferences.edit().clear().apply();
                                Intent intent = new Intent(context, PickupMain.class);
                                context.startActivity(intent);
                                activity.finish();

                            }
                        })
                        .show();

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                pDialog.dismissWithAnimation();
                sDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                sDialog.setTitleText("Error!");
                sDialog.setContentText("The details were not saved. Do you want to retry again?");
                sDialog.setCancelText("Cancel");
                sDialog.setConfirmText("Retry");
                sDialog.showCancelButton(true);
                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sDialog.dismissWithAnimation();
                        GetItems(context);

                    }
                });
                sDialog.show();

            }
        });

    }
    private void check_storage_permission(){
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},100);

            }

        }else {
            openImageChooser();
        }

    }

    private static void check_internet_conn(final Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            UploadItems(context);

        }else{
            pDialog.dismissWithAnimation();


            final SweetAlertDialog sDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            sDialog.setTitleText("Error!");
            sDialog.setContentText("No internet connection. check your connection and click retry");
            sDialog.setCancelText("Cancel");
            sDialog.setCancelable(false);
            sDialog.setConfirmText("Retry");
            sDialog.showCancelButton(true);
            sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sDialog.dismissWithAnimation();
                    GetItems(context);


                }
            });
            pDialog.show();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openImageChooser();

        }else {
            popDialog=new AlertDialog.Builder(this.getContext());
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
}
