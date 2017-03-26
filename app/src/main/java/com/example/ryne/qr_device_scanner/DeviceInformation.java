package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import model.Device;

public class DeviceInformation extends AppCompatActivity {
    private Device device = null;

    private Toolbar toolbar;
    private TextView tvNameDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        tvNameDevice = (TextView)findViewById(R.id.nameDevice);
        initToolBar();
        device = new Device();

        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("objDevice");

        Log.d("device", device.toString());
        tvNameDevice.setText(device.getDescription());
        setValueForItem();
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
    public void initView(){

    }
    public void setValueForItem(){
      // tvNameDevice.setText(device.getCountry());
    }
}
