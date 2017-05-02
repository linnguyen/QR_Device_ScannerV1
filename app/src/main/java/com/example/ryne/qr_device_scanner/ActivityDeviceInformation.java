package com.example.ryne.qr_device_scanner;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import component.Form;
import data.HttpHandler;
import data.JSONDeviceParser;
import model.Device;
import model.InventorySeason;

public class ActivityDeviceInformation extends AppCompatActivity {
    private TextView name;
    private TextView parentCode;
    private TextView origin;
    private TextView dateOfProduce;
    private TextView staff;
    private TextView digital;
    private Button btDaSeason;

    private Device objDevice = null;
    private Toolbar toolbar;
    private Date date;
    private InventorySeason inventorySeasonSelected;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        initToolBar();
        objDevice = new Device();
        inventorySeasonSelected = new InventorySeason();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            objDevice = (Device) intent.getSerializableExtra("objDevice");
        }
        name = (TextView) findViewById(R.id.nameDevice);
        parentCode = (TextView) findViewById(R.id.parentCode);
        origin = (TextView) findViewById(R.id.origin);
        dateOfProduce = (TextView) findViewById(R.id.dateOfProduce);
        staff = (TextView) findViewById(R.id.staff);
        digital = (TextView) findViewById(R.id.digital);

         name.setText(objDevice.getName());
         parentCode.setText(objDevice.getParentcode());
         origin.setText(objDevice.getCountry());
         String dateFormat = "";
        try {
            SimpleDateFormat destFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateFormat = destFormat.format(objDevice.getDateofProduce());
     //      dateFormat = destFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
         dateOfProduce.setText(objDevice.getDateofProduce());
         staff.setText(objDevice.getStaff());
        //digital.setTextColor(Color.parseColor("#212121"));
//        digital.setText("Digital:\n"+device.getDigital());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        MenuItem myInventoryMenuItem = menu.findItem(R.id.action_inventory);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.miInventory:
                Intent intent = new Intent(ActivityDeviceInformation.this, ActivityInventorySeason.class);
                intent.putExtra("device_code", objDevice.getParentcode());
                intent.putExtra("room_code", objDevice.getRoom() );
                intent.putExtra("name",objDevice.getName());
                startActivity(intent);
                overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolBar(){
        toolbar = (Toolbar)this.findViewById(R.id.toolBarDeviceInformation);
        toolbar.setTitle(R.string.toolbarTitle);
        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDeviceInformation.this, ActivityQRScanner.class);
                startActivity(intent);
            }
        });
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
