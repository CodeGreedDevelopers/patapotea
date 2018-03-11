package Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codegreeddevelopers.patapotea.R;
import customfonts.EditText_OpenSans_SemiBold;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Fragment_Add_Item2 extends android.support.v4.app.Fragment {
    private View view;
    SharedPreferences items_preferences;
    SharedPreferences.Editor editor;

    public static EditText_OpenSans_SemiBold founder_id,founder_name,founder_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_item2, container, false);

        // obtain an instance of the SharedPreferences class
        items_preferences = this.getActivity().getSharedPreferences("AddItem", MODE_PRIVATE);

        //set default values for items_preferences
        editor= items_preferences.edit();
        editor.putString("founder_name","");
        editor.putString("founder_phone","");
        editor.putString("founder_id","");
        editor.apply();

        founder_name = view.findViewById(R.id.founder_name);
        founder_phone = view.findViewById(R.id.founder_phone);
        founder_id = view.findViewById(R.id.founder_id);


        founder_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current_founder_name=founder_name.getText().toString();
                //update the details to items_preferences
                editor= items_preferences.edit();
                editor.putString("founder_name",current_founder_name);
                editor.apply();

            }
        });

        founder_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current_founder_phone=founder_phone.getText().toString();
                //update the details to items_preferences
                editor= items_preferences.edit();
                editor.putString("founder_phone",current_founder_phone);
                editor.apply();

            }
        });

        founder_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current_founder_id=founder_id.getText().toString();
                //update the details to items_preferences
                editor= items_preferences.edit();
                editor.putString("founder_id",current_founder_id);
                editor.apply();

            }
        });

        return view;
    }
    public static void ChangeBackgroundDrawable(String the_item){

        if (the_item=="founder_name"){
            founder_name.setBackgroundResource(R.drawable.spinner_background_error);

        }else if (the_item=="founder_phone"){
            founder_phone.setBackgroundResource(R.drawable.spinner_background_error);

        }else if (the_item=="founder_id"){
            founder_id.setBackgroundResource(R.drawable.spinner_background_error);

        }else{
            founder_name.setBackgroundResource(R.drawable.spinner_background);
            founder_phone.setBackgroundResource(R.drawable.spinner_background);
            founder_id.setBackgroundResource(R.drawable.spinner_background);
        }


    }
}
