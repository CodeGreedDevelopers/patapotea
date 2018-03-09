package com.codegreeddevelopers.patapotea.ListViewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by FakeJoker on 09/03/2018.
 */

public class DataGetter{
    public String item_type;
    public String item_number;
    public String item_name;
    public String dateFound;
    public String item_image;

    public DataGetter(JSONObject object){
        try {
            this.item_type=object.getString("item_type");
            this.item_number=object.getString("item_number");
            this.item_name=object.getString("item_number");
            this.dateFound=object.getString("dateFound");
            this.item_image=object.getString("item_image");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    //end of the setter part
    public static ArrayList<DataGetter> fromJson(JSONArray jsonArray){
        ArrayList<DataGetter> items_found = new ArrayList<DataGetter>();

        for (int i = 0; i <jsonArray.length(); i++) {

            try {

                items_found.add(new DataGetter(jsonArray.getJSONObject(i)));

            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
        return items_found;
    }
}
