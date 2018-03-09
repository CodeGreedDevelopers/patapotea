package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.codegreeddevelopers.patapotea.R;

import java.util.ArrayList;
import java.util.List;

import customfonts.EditText_OpenSans_SemiBold;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Fragment_Add_Item1 extends android.support.v4.app.Fragment {
    private View view;

    Spinner sp_item_type;
    EditText_OpenSans_SemiBold item_number,item_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_item1, container, false);

        sp_item_type = view.findViewById(R.id.sp_item_type);
        item_name = view.findViewById(R.id.item_name);
        item_number = view.findViewById(R.id.item_number);

        List<String> list = new ArrayList<>();
        list.add("National ID ");
        list.add("Visa Card ");
        list.add("Student ID");
        list.add("Passport");
        list.add("ATM Card");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, R.id.spinner_text, list);
        sp_item_type.setAdapter(dataAdapter);


        return view;
    }
}
