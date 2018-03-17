package com.codegreeddevelopers.patapotea.ListViewData;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codegreeddevelopers.patapotea.R;
import com.squareup.picasso.Picasso;

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

        TextView itemType =  convertView.findViewById(R.id.item_type);
        TextView itemNumber = convertView.findViewById(R.id.document_number);
        TextView itemName = convertView.findViewById(R.id.document_name);
        TextView dateFound = convertView.findViewById(R.id.found_date);
        ImageView imageView=convertView.findViewById(R.id.item_image);

        if (list_item_data.item_type.trim().equals("ATM Card")){
            Picasso.with(getContext()).load(R.drawable.credit_card_icon).into(imageView);
        }else if(list_item_data.item_type.trim().equals("Student ID")){
            Picasso.with(getContext()).load(R.drawable.student_id_card).into(imageView);
        }else if(list_item_data.item_type.trim().equals("Passport")){
            Picasso.with(getContext()).load(R.drawable.passport_icon).into(imageView);
        }else if (list_item_data.item_type.trim().equals("Visa Card")){
            Picasso.with(getContext()).load(R.drawable.visa_card_icon).into(imageView);
        }else if (list_item_data.item_type.trim().equals("Driving Licence")){
            Picasso.with(getContext()).load(R.drawable.driver_licence).into(imageView);
        } else{
            Picasso.with(getContext()).load(R.drawable.national_card).into(imageView);
        }



        // Populate the data into the template view using the data object
        itemType.setText(list_item_data.item_type);

        itemNumber.setText(list_item_data.item_number);

        itemName.setText(list_item_data.item_name);

        dateFound.setText(list_item_data.dateFound);

        // Return the completed view to render on screen

        return convertView;

    }
}
