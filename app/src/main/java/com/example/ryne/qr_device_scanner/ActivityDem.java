package com.example.ryne.qr_device_scanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import model.Device;

public class ActivityDem extends AppCompatActivity {
    private ListView listView;
    private AdapterDemoList adapter;
    private List<Device> deviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dem);

        listView = (ListView)this.findViewById(R.id.listView);
        deviceList = new ArrayList<>();

        Device device = new Device("","","","Viet Nam","","","","","","This is very beautiful computer in It department used for blah blah blah blah blah");
        deviceList.add(device);

        adapter = new AdapterDemoList(ActivityDem.this,deviceList);
        listView.setAdapter(adapter);
    }
}
