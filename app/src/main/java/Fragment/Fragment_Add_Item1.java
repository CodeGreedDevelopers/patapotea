package Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.codegreeddevelopers.patapotea.R;

import java.util.ArrayList;
import java.util.List;

import customfonts.EditText_OpenSans_SemiBold;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Fragment_Add_Item1 extends android.support.v4.app.Fragment {
    private View view;
    SharedPreferences items_preferences;
    SharedPreferences.Editor editor;

    Spinner sp_item_type;
    public static EditText_OpenSans_SemiBold item_number,item_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_item1, container, false);

        // obtain an instance of the SharedPreferences class
        items_preferences = this.getActivity().getSharedPreferences("AddItem", MODE_PRIVATE);

        //set default values for items_preferences
        editor= items_preferences.edit();
        editor.putString("selected_item","");
        editor.putString("item_number","");
        editor.putString("item_name","");
        editor.apply();

        sp_item_type = view.findViewById(R.id.sp_item_type);
        item_name = view.findViewById(R.id.item_name);
        item_number = view.findViewById(R.id.item_number);


        List<String> list = new ArrayList<>();
        list.add("National ID ");
        list.add("Visa Card ");
        list.add("Student ID");
        list.add("Passport");
        list.add("ATM Card");
        list.add("Driving Licence");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, R.id.spinner_text, list);
        sp_item_type.setAdapter(dataAdapter);

        sp_item_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item=sp_item_type.getSelectedItem().toString();
                //update the selected item to preference after an item is selected
                editor= items_preferences.edit();
                editor.putString("item_type",selected_item);
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        item_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current_item_name=item_name.getText().toString();
                //update the selected item to preference after an item is selected
                editor= items_preferences.edit();
                editor.putString("item_name",current_item_name);
                editor.apply();

            }
        });

        item_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current_item_number=item_number.getText().toString();
                //update the selected item to preference after an item is selected
                editor= items_preferences.edit();
                editor.putString("item_number",current_item_number);
                editor.apply();

            }
        });


        return view;
    }
    public static void ChangeBackgroundDrawable(String the_item){

        if (the_item=="item_name"){
            item_name.setBackgroundResource(R.drawable.spinner_background_error);

        }else if (the_item=="item_number"){
            item_number.setBackgroundResource(R.drawable.spinner_background_error);

        }else{
            item_name.setBackgroundResource(R.drawable.spinner_background);
            item_number.setBackgroundResource(R.drawable.spinner_background);
        }


    }

}
