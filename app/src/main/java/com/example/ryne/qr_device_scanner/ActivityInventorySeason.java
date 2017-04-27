package com.example.ryne.qr_device_scanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.AdapterSeason;
import adapter.AdapterWareHouse;
import model.Device;

public class ActivityInventorySeason extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private ListView listView;

    private AdapterSeason adapterSeason;
    private ArrayList<Device> arrlistDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_season);
        initListView();
    }

    public void initListView(){
        listView = (ListView) findViewById(R.id.lvSeason);
        arrlistDevice = new ArrayList<>();
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Asus","A001"));
        arrlistDevice.add(new Device("Asus","A001"));
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("HP","D001"));
        arrlistDevice.add(new Device("Acer","D001"));
        adapterSeason = new AdapterSeason(arrlistDevice, getApplicationContext());
        listView.setAdapter(adapterSeason);
    }
}
