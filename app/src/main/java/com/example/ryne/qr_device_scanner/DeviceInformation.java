package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterDeviceInformation;
import data.JSONDeviceParser;
import model.Device;

public class DeviceInformation extends AppCompatActivity {
    private Device device = null;

    private Toolbar toolbar;
    private TextView tvNameDevice;
    private ListView listViewDevice;
    private AdapterDeviceInformation adapterDeviceInformation;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        initToolBar();
        listViewDevice = (ListView) findViewById(R.id.listViewDevice);
        // initual
        device = new Device();
        deviceList = new ArrayList<>();

        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("objDevice");
        deviceList.add(device);

        adapterDeviceInformation = new AdapterDeviceInformation(DeviceInformation.this, deviceList);
        listViewDevice.setAdapter(adapterDeviceInformation);
        //  Log.d("device", device.toString()+ device.getDateofProduce());
    }
    public void initToolBar(){
        toolbar = (Toolbar)this.findViewById(R.id.toolBarDeviceInformation);
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
