package com.codegreeddevelopers.patapotea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PickPointMainActivity extends AppCompatActivity {
    Button add_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_point_main);
        add_item=findViewById(R.id.add_item);

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PickPointMainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
    }
}
