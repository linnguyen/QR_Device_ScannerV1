package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.AdapterDeviceInformation;
import data.JSONDeviceParser;
import model.Device;

public class DeviceInformation extends AppCompatActivity {
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

        name.setText(device.getName());
        parentCode.setText(device.getParentcode());
        origin.setText(device.getCountry());
        String dateFormat = "";
        try {
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat destFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = sourceFormat.parse(device.getDateofProduce());
            dateFormat = destFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateOfProduce.setText(dateFormat);
        staff.setText(device.getStaff());
        //digital.setTextColor(Color.parseColor("#212121"));
        digital.setText("Digital:\n"+device.getDigital());
    }
    public void initToolBar(){
        toolbar = (Toolbar)this.findViewById(R.id.toolBarDeviceInformation);
        toolbar.setTitle(R.string.toolbarTitle);
        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceInformation.this, QRScanner.class);
                startActivity(intent);
            }
        });
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
