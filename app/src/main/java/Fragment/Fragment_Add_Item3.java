package Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.codegreeddevelopers.patapotea.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Fragment_Add_Item3 extends android.support.v4.app.Fragment {
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    private View view;
    public static String item_type,item_number,item_name,founder_name,founder_phone,founder_id,result;
    ImageView item_image;
    AlertDialog.Builder popDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_item3, container, false);

        // obtain an instance of the SharedPreferences class
        preferences= this.getActivity().getSharedPreferences("AddItem", MODE_PRIVATE);

        item_image=view.findViewById(R.id.item_image);

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_permission();

            }
        });





        return view;
    }
    public static void GetItems(Context context){
        item_type = preferences.getString("item_type", null);
        item_number = preferences.getString("item_number", null);
        item_name = preferences.getString("item_name", null);
        founder_name = preferences.getString("founder_name", null);
        founder_phone = preferences.getString("founder_phone", null);
        founder_id = preferences.getString("founder_id", null);

        Toast.makeText(context, "Type: "+item_type+" Number: "+item_number+" Name: "+item_name+" Phone: "+founder_phone+" ID: "+founder_id, Toast.LENGTH_LONG).show();
        UploadItems(context);
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

        try {
            params.put("item_image", new File(result));
            params.put("item_name", item_name);
            params.put("item_number", item_number);
            params.put("item_type", item_type);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://www.duma.com/patapotea/items_data_setter.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(context, "Success uploading...", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(context, "Error uploading...", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void check_permission(){
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},100);

            }

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
                            check_permission();

                        }
                    });
            popDialog.create().show();



        }
    }
}
