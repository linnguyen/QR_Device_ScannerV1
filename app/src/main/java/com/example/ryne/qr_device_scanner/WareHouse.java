package com.example.ryne.qr_device_scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.AdapterInventory;
import adapter.AdapterWareHouse;
import model.Device;

public class WareHouse extends AppCompatActivity {
    private Context context;
    private Toolbar toolbar;
    private ListView listView;

    private AdapterWareHouse adapterWareHouse;
    private ArrayList<Device> arrlistDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house);
        initToolBar();
        initListView();
    }
    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolBarWareHouse);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
        toolbar.setTitle("Back");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WareHouse.this, QRScanner.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    adapterWareHouse.filter("");
                    listView.clearTextFilter();
                }else{
                      adapterWareHouse.filter(newText);
                }
                return true;
            }
        });
        return true;
    }
    public void initListView(){
        listView = (ListView) findViewById(R.id.listViewWareHouse);
        arrlistDevice = new ArrayList<>();
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Asus","A001"));
        arrlistDevice.add(new Device("Asus","A001"));
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("HP","D001"));
        arrlistDevice.add(new Device("Acer","D001"));
        adapterWareHouse = new AdapterWareHouse(arrlistDevice,getApplicationContext());
        listView.setAdapter(adapterWareHouse);
    }
}
