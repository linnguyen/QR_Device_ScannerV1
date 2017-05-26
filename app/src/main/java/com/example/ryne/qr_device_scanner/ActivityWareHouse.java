package com.example.ryne.qr_device_scanner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.AdapterWareHouse;
import data.HttpHandler;
import data.JSONDeviceParser;
import model.Device;
import utils.Config;

public class ActivityWareHouse extends AppCompatActivity {
    private Context context;
    private Toolbar toolbar;
    private ListView listView;
    ProgressBar pgBar;

    private AdapterWareHouse adapterWareHouse;
    private ArrayList<Device> arrlistDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house);
        initToolBar();
        arrlistDevice = new ArrayList<>();
        WareHouseDataTasks wareHouseDataTasks = new WareHouseDataTasks(ActivityWareHouse.this);
        wareHouseDataTasks.execute("/devices_left");
//        initListView();
    }
    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolBarWareHouse);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
        toolbar.setTitle("Quay lại");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityWareHouse.this, ActivityQRScanner.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
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
        adapterWareHouse = new AdapterWareHouse(arrlistDevice,getApplicationContext());
        listView.setAdapter(adapterWareHouse);
    }

      private class WareHouseDataTasks extends AsyncTask<String, Void, String> {
        private Context context;
        public WareHouseDataTasks(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            pgBar = (ProgressBar) findViewById(R.id.pgBarWareHouse);
            pgBar.setVisibility(ProgressBar.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String dataWarehouse = HttpHandler.makeServiceCall(params[0]);
            return dataWarehouse;
        }

        @Override
        protected void onPostExecute(String dataWarehouse) {
            pgBar.setVisibility(ProgressBar.GONE);
            arrlistDevice = JSONDeviceParser.getDeviceLeft(dataWarehouse);
            initListView();
        }
    }
}
