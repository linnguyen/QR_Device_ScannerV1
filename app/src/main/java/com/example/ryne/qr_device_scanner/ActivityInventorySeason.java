package com.example.ryne.qr_device_scanner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.AdapterSeason;
import data.HttpHandler;
import data.JSONDeviceParser;
import model.InventorySeason;

public class ActivityInventorySeason extends AppCompatActivity {
    private Context context;
    private Toolbar toolbar;
    private ListView listView;
    private EditText edSelectSeason;
    private ProgressBar pgBar;

    private AdapterSeason adapterSeason;
    private ArrayList<InventorySeason> arrSeason;
    private String device_code;
    private String room_code;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_season);
        //initToolBar();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            device_code = intent.getExtras().getString("device_code");
            room_code = intent.getExtras().getString("room_code");
            name = intent.getExtras().getString("name");
        }
        arrSeason = new ArrayList<>();
//        InventorySeasonDataTasks inventorySeasonDataTasks = new InventorySeasonDataTasks(ActivityInventorySeason.this);
//        inventorySeasonDataTasks.execute("/inventory_seasons");
        initListView();
    }

    public void initListView() {
        listView = (ListView) findViewById(R.id.lvSeason);
        arrSeason.add(new InventorySeason(1,"cuối năm"));
        arrSeason.add(new InventorySeason(2,"thường xuyên"));
        arrSeason.add(new InventorySeason(2,"đột xuất"));
        adapterSeason = new AdapterSeason(arrSeason, getApplicationContext());
        listView.setAdapter(adapterSeason);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view = listView.getChildAt(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbSeasonSelect);
                EditText edIdSeasonSelect = (EditText) view.findViewById(R.id.edIdSeasonSelect);
                InventorySeason ivSeason = (InventorySeason) edIdSeasonSelect.getTag();
                checkBox.setChecked(true);
                if (device_code == null) {
                    Intent intent = new Intent(ActivityInventorySeason.this, ActivityInventoryPerRoom.class);
                    intent.putExtra("id_dot", ivSeason.getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityInventorySeason.this, ActivityInventoryPerDevice.class);
                    intent.putExtra("id_dot", ivSeason.getId());
                    intent.putExtra("device_code", device_code);
                    intent.putExtra("room_code", room_code);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

//    private class InventorySeasonDataTasks extends AsyncTask<String, Void, String>{
//        private Context context;
//        public InventorySeasonDataTasks(Context context) {
//            this.context = context;
//        }
//
//        protected void onPreExecute() {
//            pgBar = (ProgressBar) findViewById(R.id.pgBarSeaSon);
//            pgBar.setVisibility(ProgressBar.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String dataInventorySeason = HttpHandler.makeServiceCall(params[0]);
//            return dataInventorySeason;
//        }
//
//        @Override
//        protected void onPostExecute(String dataInventorySeason) {
//            pgBar.setVisibility(ProgressBar.GONE);
//            arrSeason = JSONDeviceParser.getInventorySeason(dataInventorySeason);
//            initListView();
//        }
//    }
}
