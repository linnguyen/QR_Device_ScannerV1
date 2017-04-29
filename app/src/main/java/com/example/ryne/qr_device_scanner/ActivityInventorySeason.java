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

    private AdapterSeason adapterSeason;
    private ArrayList<InventorySeason> arrSeason;
//  xoa di
    private String macode;
    private String room_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_season);
        //initToolBar();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            macode = intent.getExtras().getString("device_code");
            room_code = intent.getExtras().getString("room_code");
            Log.d("room_ne", room_code);
        }
        arrSeason = new ArrayList<>();
        InventorySeasonDataTasks inventorySeasonDataTasks = new InventorySeasonDataTasks(ActivityInventorySeason.this);
        inventorySeasonDataTasks.execute("/inventory_seasons");
    }
//    public void initToolBar(){
//        toolbar = (Toolbar) findViewById(R.id.toolBarInventorySeason);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
//        toolbar.setTitle("Quay lại");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityInventorySeason.this, ActivityQRScanner.class);
//                startActivity(intent);
//            }
//        });
//    }
    public void initListView(){
        listView = (ListView) findViewById(R.id.lvSeason);
//        arrSeason.add(new InventorySeason(1,"Giờ vàng"));
//        arrSeason.add(new InventorySeason(2,"Giờ cao điểm"));
        adapterSeason = new AdapterSeason(arrSeason, getApplicationContext());
        listView.setAdapter(adapterSeason);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view = listView.getChildAt(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbSeasonSelect);
                EditText edIdSeasonSelect = (EditText) view.findViewById(R.id.edIdSeasonSelect);
                InventorySeason inventorySeason = (InventorySeason) edIdSeasonSelect.getTag();
                checkBox.setChecked(true);
                if(macode == null){
                    Intent intent = new Intent(ActivityInventorySeason.this, ActivityInventoryPerRoom.class);
                    intent.putExtra("id_dot", inventorySeason.getId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ActivityInventorySeason.this, ActivityInventoryPerDevice.class);
                    intent.putExtra("id_dot", inventorySeason.getId());
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    private class InventorySeasonDataTasks extends AsyncTask<String, Void, String> {
        private Context context;
        public  InventorySeasonDataTasks(Context context){
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String dataInventorySeason = HttpHandler.makeServiceCall(params[0]);
            return dataInventorySeason;
        }
        @Override
        protected void onPostExecute(String dataInventorySeason) {
            arrSeason = JSONDeviceParser.getInventorySeason(dataInventorySeason);
            initListView();
        }
    }
}
