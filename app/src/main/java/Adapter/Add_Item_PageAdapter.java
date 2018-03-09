package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import Fragment.Fragment_Add_Item1;
import Fragment.Fragment_Add_Item2;
import Fragment.Fragment_Add_Item3;

/**
 * Created by Augustine on 3/9/2018.
 */

public class Add_Item_PageAdapter extends FragmentStatePagerAdapter {
    public Add_Item_PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment_Add_Item1 tab1 = new Fragment_Add_Item1();
                return tab1;

            case 1:
                Fragment_Add_Item2 tab2 = new Fragment_Add_Item2();
                return tab2;

            case 2:
                Fragment_Add_Item3 tab3 = new Fragment_Add_Item3();
                return tab3;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
