package com.codegreeddevelopers.patapotea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    ListView items_List;
    String[] items={"Manyasa","Oliver","watiti","Kurshny","Michael","Wesonga"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items_List=findViewById(R.id.items_list);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);

        items_List.setAdapter(arrayAdapter);
        get_list_items_online();
    }
    public void get_list_items_online(){
        AsyncHttpClient  httpClient=new AsyncHttpClient();
        httpClient.get("http://www.duma.co.ke/patapotea/items_data_getter.php", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MainActivity.this,responseString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(MainActivity.this,responseString, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
