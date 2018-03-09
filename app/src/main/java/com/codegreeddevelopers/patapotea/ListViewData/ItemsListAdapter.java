package com.codegreeddevelopers.patapotea.ListViewData;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codegreeddevelopers.patapotea.R;

import java.util.List;

/**
 * Created by FakeJoker on 09/03/2018.
 */

public class ItemsListAdapter extends ArrayAdapter<DataGetter> {

    public ItemsListAdapter(Context context, int resource,List<DataGetter> objects) {
        super(context, resource, objects);
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        DataGetter list_item_data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_list_row_display, parent, false);

        }

        // Lookup view for data population

        TextView itemType = (TextView) convertView.findViewById(R.id.item_type);
        TextView itemNumber = (TextView) convertView.findViewById(R.id.document_number);
        TextView itemName = (TextView) convertView.findViewById(R.id.document_name);
        TextView dateFound = (TextView) convertView.findViewById(R.id.found_date);

        // Populate the data into the template view using the data object
        itemType.setText(list_item_data.item_type);

        itemNumber.setText("Number: "+list_item_data.item_number);

        itemName.setText(list_item_data.item_name);

        dateFound.setText(list_item_data.dateFound);

        // Return the completed view to render on screen

        return convertView;

    }
}
