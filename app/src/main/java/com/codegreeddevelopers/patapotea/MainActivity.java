package com.codegreeddevelopers.patapotea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.codegreeddevelopers.patapotea.Item_details.ItemsDetailsActivity;
import com.codegreeddevelopers.patapotea.ListViewData.DataGetter;
import com.codegreeddevelopers.patapotea.ListViewData.ItemsListAdapter;
import com.codegreeddevelopers.patapotea.ListViewData.Suggestion;
import com.codegreeddevelopers.patapotea.MapActivity.MapActivity;
import com.gturedi.views.StatefulLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import mehdi.sakout.dynamicbox.DynamicBox;

public class MainActivity extends AppCompatActivity {
    ListView items_List;
    ItemsListAdapter itemsListAdapter;
    FloatingSearchView mSearchView;
    DynamicBox dynamicBox;
    StatefulLayout statefulLayout;
    List<Suggestion> suggestions_list;
    SweetAlertDialog searching_dialog;
    String myjsonData="",all_items="";
    AsyncHttpClient  httpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        suggestions_list = new ArrayList<>();
        //suggestions_list.add(new Suggestion("Manyasa"));


        //async httpclient initialization
        httpClient=new AsyncHttpClient();
        items_List=findViewById(R.id.items_list);
        mSearchView=findViewById(R.id.floating_search_view);
        dynamicBox=new DynamicBox(MainActivity.this,items_List);
        statefulLayout=findViewById(R.id.stateful);



        //show the loading box
        View customView = getLayoutInflater().inflate(R.layout.loading_activity, items_List, false);
        dynamicBox.addCustomView(customView,"greenmonster");

        //searching dialog box
        searching_dialog= new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        searching_dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        searching_dialog.setTitleText("Searching Data...");
        searching_dialog.setCancelable(false);





        ArrayList<DataGetter> array_of_data=new ArrayList<>();

        itemsListAdapter=new ItemsListAdapter(MainActivity.this,0,array_of_data);


        if (connection_status()){
            get_list_items_online();
        }else{
            dynamicBox.showExceptionLayout();
            dynamicBox.setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (connection_status()){
                        get_list_items_online();
                    }else{
                        dynamicBox.showExceptionLayout();
                    }
                }
            });
        }



        //start of searching
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                mSearchView.hideProgress();
                search_data_online(searchSuggestion.getBody().toString().trim());
                mSearchView.clearSearchFocus();
            }

            @Override
            public void onSearchAction(String currentQuery) {
                mSearchView.hideProgress();
                search_data_online(currentQuery);
            }
        });




        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                    mSearchView.hideProgress();
                }else if(myjsonData.isEmpty()){
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    setting_up_search_suggerstion(all_items,newQuery);
                    mSearchView.swapSuggestions(suggestions_list);
                }
            }
        });
        mSearchView.setLeftActionIconColor(R.color.colorPrimary);

        //menu items click listener
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_pickup) {
                    Intent intent=new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                }else if (id == R.id.action_setting) {
                    Intent intent=new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }else if (id == R.id.action_share){
                    Intent share=new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT,"Search For your Lost Documents such as ID card,Passports and ATM card using This Free Android App https://play.google.com/store/apps/details?id=com.codegreed_devs.patapotea");
                    startActivity(Intent.createChooser(share,"Share Using"));
                }else if(id == R.id.action_about){
                    Intent intent2=new Intent(MainActivity.this, About.class);
                    startActivity(intent2);
                }
            }
        });



    }
    public void get_list_items_online(){
        View customView = getLayoutInflater().inflate(R.layout.loading_activity, items_List, false);
        dynamicBox.addCustomView(customView,"greenmonster");
        dynamicBox.showCustomView("greenmonster");
        //Toast.makeText(this, "starting", Toast.LENGTH_SHORT).show();
        httpClient.get("http://www.duma.co.ke/patapotea/items_data_getter.php", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //Toast.makeText(MainActivity.this,"failed", Toast.LENGTH_SHORT).show();
                dynamicBox.showExceptionLayout();
                dynamicBox.setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connection_status()){
                            get_list_items_online();
                        }else{
                            dynamicBox.showExceptionLayout();
                        }
                    }
                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Toast.makeText(MainActivity.this,responseString, Toast.LENGTH_SHORT).show();
                itemsListAdapter.clear();
                myjsonData=responseString;
                all_items=responseString;
                try {
                    JSONArray items_array=new JSONArray(responseString);
                    ArrayList<DataGetter> dataGetters=DataGetter.fromJson(items_array);
                    itemsListAdapter.addAll(dataGetters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        items_List.setAdapter(itemsListAdapter);


        //onclick listener for the listview
        items_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONArray clicked_array=new JSONArray(myjsonData);
                    JSONObject object=clicked_array.getJSONObject(position);

                    Intent intent=new Intent(MainActivity.this, ItemsDetailsActivity.class);
                    intent.putExtra("item_id",object.get("id").toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //searching data online
    public void search_data_online(String query){
        searching_dialog.show();
        RequestParams params=new RequestParams();
        params.put("query",query.toLowerCase());
        httpClient.post("http://www.duma.co.ke/patapotea/search_items.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                searching_dialog.dismiss();
                Toast.makeText(MainActivity.this, "An Error Occurred"+responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("Response :",responseString);
                searching_dialog.dismiss();
                if (responseString.trim().equals("failed")){
                    show_no_content();
                }else{
                    myjsonData=responseString;
                    itemsListAdapter.clear();
                    try {
                        JSONArray items_array=new JSONArray(responseString);
                        Log.e("Search Item Count : ",""+items_array.length());
                        ArrayList<DataGetter> dataGetters=DataGetter.fromJson(items_array);
                        itemsListAdapter.addAll(dataGetters);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //end of querring if response has data in it

            }
        });
        items_List.setAdapter(itemsListAdapter);
    }

    public Boolean connection_status(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }else {
            return false;
        }
    }

    public void show_no_content(){
        View customView = getLayoutInflater().inflate(R.layout.error_messages, items_List, false);
        statefulLayout=customView.findViewById(R.id.stateful);
        statefulLayout.showEmpty("There are No Items yet");
        dynamicBox.addCustomView(customView,"no_items");
        dynamicBox.showCustomView("no_items");
    }
    public void setting_up_search_suggerstion(String response,String query){
        suggestions_list.clear();
        try {
            JSONArray jsonArray=new JSONArray(response);
            for (int x=0;x<jsonArray.length();x++){
                JSONObject jsonObject=jsonArray.getJSONObject(x);
                if (jsonObject.get("item_type").toString().toLowerCase().contains(query)){
                    search_if_item_exist(jsonObject.get("item_type").toString());
                }

                if(jsonObject.get("item_number").toString().toLowerCase().contains(query)){
                    search_if_item_exist(jsonObject.get("item_number").toString());
                    //suggestions_list.add(new Suggestion(jsonObject.get("item_number").toString()));
                }

                if(jsonObject.get("item_name").toString().toLowerCase().contains(query)){
                    search_if_item_exist(jsonObject.get("item_name").toString());
                    //suggestions_list.add(new Suggestion(jsonObject.get("item_name").toString()));

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void search_if_item_exist(String item_name){
        //suggestions_list.clear();
        if (suggestions_list.size()==0){

            suggestions_list.add(new Suggestion(item_name));
            //Toast.makeText(this, "Adding"+suggestions_list.size(), Toast.LENGTH_SHORT).show();
        }else{
            //try {
                int count=suggestions_list.size();
                //Toast.makeText(this, ""+count, Toast.LENGTH_SHORT).show();
                for (int i=0;i<count;i++) {
                    //suggestions_list.notify();
                    String s1= new String(suggestions_list.get(i).getBody().toString().toLowerCase().trim().toCharArray());
                    String s2= new String(item_name.toLowerCase().trim().toCharArray());
                    if (Arrays.equals(s1.toCharArray(),s2.toCharArray())) {
                        return;
                    } else {
                        //Toast.makeText(this, s1+" does not match "+s2, Toast.LENGTH_SHORT).show();
                        suggestions_list.add(new Suggestion(item_name));
                        continue;
                    }
                }
//            }catch (Exception e){
//                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
        }

    }
}
