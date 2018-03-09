package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codegreeddevelopers.patapotea.R;
import customfonts.EditText_OpenSans_SemiBold;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Fragment_Add_Item2 extends android.support.v4.app.Fragment {
    private View view;

    EditText_OpenSans_SemiBold founder_id,founder_name,founder_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_item2, container, false);

        founder_phone = view.findViewById(R.id.founder_phone);
        founder_name = view.findViewById(R.id.founder_name);
        founder_id = view.findViewById(R.id.founder_id);

        return view;
    }
}
