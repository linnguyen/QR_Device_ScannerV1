package com.example.ryne.qr_device_scanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import component.Form;
import data.HttpHandler;
import data.JSONDeviceParser;
import model.Device;
import model.InventoryLab;
import model.InventorySeason;
import utils.Config;

public class ActivityDeviceInformation extends AppCompatActivity {
    private TextView name;
    private TextView parentCode;
    private TextView origin;
    private TextView dateOfProduce;
    private TextView staff;
    private TextView digital;
    private TextView manufacturer;
    private TextView timeOfWarranty;
    private Button btDaSeason;

    private Device objDevice = null;
    private Toolbar toolbar;
    private InventorySeason inventorySeasonSelected;
    private String message;
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
        manufacturer = (TextView) findViewById(R.id.manufacturer);
        timeOfWarranty = (TextView) findViewById(R.id.warranty);

         name.setText(objDevice.getName());
         parentCode.setText(objDevice.getParentcode());
         origin.setText(objDevice.getCountry());
         String dateFormat = "";
        try {
            SimpleDateFormat destFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat = destFormat.format(destFormat.parse(objDevice.getDateofProduce()));
        } catch (Exception e) {
            e.printStackTrace();
        }
         dateOfProduce.setText(objDevice.getDateofProduce());
         staff.setText(objDevice.getStaff());
         digital.setText(objDevice.getDescription());
         manufacturer.setText(objDevice.getProducer());
         timeOfWarranty.setText(objDevice.getTimeofWarranty());
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
                CheckLatestInventoryRequest checkLatestInventoryRequest = new CheckLatestInventoryRequest();
                checkLatestInventoryRequest.execute(objDevice.getParentcode());
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

    private class CheckLatestInventoryRequest extends AsyncTask<String, Void, String> {
        OutputStream outputStream;
        BufferedWriter bufferedWriter;
        JSONObject jsonObjectDevice;
        JSONArray jsonArrayDevice;
        JSONObject postParams;

        @Override
        protected String doInBackground(String... params) {
            try {
                // URL url = new URL("https://apiqrcode-v1.herokuapp.com/inventories");
                URL url = new URL(Config.URL + "/inventories/latest_inventory_for_device");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                String codeParent = params[0];
                postParams = new JSONObject();
                postParams.put("ma_thiet_bi", codeParent);
                outputStream = connection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(String.valueOf(postParams));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            message = JSONDeviceParser.getMessageResponse(s);
            if (message.equals("")) {
                navigateSeasonActivity();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDeviceInformation.this);
                builder.setMessage(message)
                        .setTitle("Xác Nhận")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                navigateSeasonActivity();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                            }
                        }).show();
            }
        }


        public void navigateSeasonActivity() {
            Intent intent = new Intent(ActivityDeviceInformation.this, ActivityInventorySeason.class);
            intent.putExtra("device_code", objDevice.getParentcode());
            intent.putExtra("room_code", objDevice.getRoom() );
            intent.putExtra("name",objDevice.getName());
            startActivity(intent);
            overridePendingTransition(R.anim.top_in, R.anim.bottom_out);

        }
     }
}
