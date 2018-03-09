package com.codegreeddevelopers.patapotea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.codegreeddevelopers.patapotea.ListViewData.DataGetter;
import com.codegreeddevelopers.patapotea.ListViewData.ItemsListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mehdi.sakout.dynamicbox.DynamicBox;

public class MainActivity extends AppCompatActivity {
    ListView items_List;
    ItemsListAdapter itemsListAdapter;
    FloatingSearchView mSearchView;
    DynamicBox dynamicBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items_List=findViewById(R.id.items_list);
        mSearchView=findViewById(R.id.floating_search_view);
        dynamicBox=new DynamicBox(MainActivity.this,items_List);


        //show the loading box
        View customView = getLayoutInflater().inflate(R.layout.loading_activity, items_List, false);
        dynamicBox.addCustomView(customView,"greenmonster");
        dynamicBox.showCustomView("greenmonster");



        ArrayList<DataGetter> array_of_data=new ArrayList<>();

        itemsListAdapter=new ItemsListAdapter(MainActivity.this,0,array_of_data);



        //get_list_items_online();

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                search_data_online(newQuery);
            }
        });



    }
    public void get_list_items_online(){
        Toast.makeText(this, "starting", Toast.LENGTH_SHORT).show();
        AsyncHttpClient  httpClient=new AsyncHttpClient();
        httpClient.get("http://www.duma.co.ke/patapotea/items_data_getter.php", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this,"failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(MainActivity.this,responseString, Toast.LENGTH_SHORT).show();
                itemsListAdapter.clear();
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
    }
    //searching data online
    public void search_data_online(String query){

        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("query",query);
        asyncHttpClient.post("http://www.duma.co.ke/patapotea/search_items.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Toast.makeText(MainActivity.this,responseString, Toast.LENGTH_SHORT).show();
                itemsListAdapter.clear();
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
    }
}
