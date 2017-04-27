package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Device;

public class ActivityDeviceInformation extends AppCompatActivity {
    private Device device = null;
    private Toolbar toolbar;
    private Date date;

    private TextView name;
    private TextView parentCode;
    private TextView origin;
    private TextView dateOfProduce;
    private TextView staff;
    private TextView digital;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);
        initToolBar();
        device = new Device();
        Intent intent = getIntent();
        device = (Device) intent.getSerializableExtra("objDevice");
        name = (TextView) findViewById(R.id.nameDevice);
        parentCode = (TextView) findViewById(R.id.parentCode);
        origin = (TextView) findViewById(R.id.origin);
        dateOfProduce = (TextView) findViewById(R.id.dateOfProduce);
        staff = (TextView) findViewById(R.id.staff);
        digital = (TextView) findViewById(R.id.digital);

//        name.setText(device.getName());
//        parentCode.setText(device.getParentcode());
         //origin.setText(device.getCountry());
        String dateFormat = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat destFormat = new SimpleDateFormat("dd-MM-yyyy");
//            date = sourceFormat.parse(device.getDateofProduce());
            dateFormat = destFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //dateOfProduce.setText(dateFormat);
        //staff.setText(device.getStaff());
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

    public void showFormInvenrory(){

    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }


}
