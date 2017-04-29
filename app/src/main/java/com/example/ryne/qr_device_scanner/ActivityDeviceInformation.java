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
    // xoa di
    private String device_code = "Fdldd";
    private String room_code = "D021";
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
//                Form.showDialogInventorySeasonSelect(ActivityDeviceInformation.this);
//                Form.btDaSeason.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        inventorySeasonSelected = Form.inventorySeasonSelected;
//                        if (inventorySeasonSelected != null) {
//                           // Log.d("ses ne", inventorySeasonSelected.toString());
//                            Toast.makeText(ActivityDeviceInformation.this, "Duoc ma", Toast.LENGTH_LONG).show();
//                            showFormInvenrory();
//                        }else{
//                            Form.openDialog.dismiss();
//                            Toast.makeText(ActivityDeviceInformation.this, "Vui lòng chọn đợt kiểm kê", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
                Intent intent = new Intent(ActivityDeviceInformation.this, ActivityInventorySeason.class);
                intent.putExtra("device_code", device_code);
                intent.putExtra("room_code", room_code );
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

    public void showFormInvenrory(){
        Dialog openDialog = new Dialog(ActivityDeviceInformation.this);
        //openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_row_inventory);
        openDialog.setTitle("Thông Tin Kiểm Kê Thiết Bị");
        openDialog.show();
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
