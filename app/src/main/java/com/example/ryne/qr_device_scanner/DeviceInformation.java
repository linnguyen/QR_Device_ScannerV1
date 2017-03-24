package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import model.Device;

public class DeviceInformation extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        initToolBar();
        Device device = (Device) getIntent().getParcelableExtra("objDevice");
        device.getName();
        Log.d("namedevice", device.getName());
    }
    public void initToolBar(){
        toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbarTitle);
        toolbar.setNavigationIcon(R.drawable.left_arrow_small);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceInformation.this, QRScanner.class);
                startActivity(intent);
            }
        });
    }
}
