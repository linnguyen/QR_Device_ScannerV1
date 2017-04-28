package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ActivityInventoryPerDevice extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_per_device);
        initToolBar();
    }
    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolBarInventoryPerDevice);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
        //toolbar.setTitle("Quay l");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityWareHouse.this, ActivityQRScanner.class);
//                startActivity(intent);
//            }
//        });
    }

}
