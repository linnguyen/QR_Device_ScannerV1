package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import model.InventoryLab;
import utils.Config;

public class ActivityInventoryPerDevice extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvName;
    private EditText edNumberOfDeviceLeft;
    private EditText edNumberOfNormalDevice;
    private EditText edNumberOfBrokenDevice;
    private EditText edNumberOfUnusedDevice;
    private EditText edNoteDevice;
    private Button btSubmitDevice;

    private int id_dot;
    private String name;
    private String device_code;
    private String room_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_per_device);
        initToolBar();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            id_dot = intent.getExtras().getInt("id_dot");
            name = intent.getExtras().getString("name");
            device_code = intent.getExtras().getString("device_code");
            room_code = intent.getExtras().getString("room_code");
        }

        tvName = (TextView) findViewById(R.id.tvNameDeviceByOne);
        edNumberOfDeviceLeft = (EditText) findViewById(R.id.edNumberOfDeviceLeftByOne);
        edNumberOfNormalDevice = (EditText) findViewById(R.id.edNumberOfNormalDeviceByOne);
        edNumberOfBrokenDevice = (EditText) findViewById(R.id.edNumberOfBrokenDeviceByOne);
        edNumberOfUnusedDevice = (EditText) findViewById(R.id.edNumberOfUnusedDeviceByOne);
        edNoteDevice = (EditText) findViewById(R.id.edNoteDeviceByOne);
        btSubmitDevice = (Button) findViewById(R.id.btSubmitByOne);
        tvName.setText(name);

        btSubmitDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPostRequest sendPostRequest = new SendPostRequest();
                sendPostRequest.execute(getDataDevice());
            }
        });
    }
    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolBarInventoryPerDevice);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.left_arrow_white);
        //toolbar.setTitle("Quay l");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityWareHouse.this, ActivityQRScanner.class);
//                startActivity(intent);
//            }
//        });
    }
    public InventoryLab getDataDevice(){
         int numberOfDeviceLeft = Integer.parseInt(edNumberOfDeviceLeft.getText().toString());
         int numberOfNormalDevice = Integer.parseInt(edNumberOfNormalDevice.getText().toString());
         int numberOfBrokenDevice = Integer.parseInt(edNumberOfBrokenDevice.getText().toString());
         int numberOfUnusedDevice = Integer.parseInt(edNumberOfUnusedDevice.getText().toString());
         String noteDevice = edNoteDevice.getText().toString();
         return new InventoryLab(device_code, numberOfDeviceLeft, numberOfNormalDevice, numberOfBrokenDevice,
         numberOfUnusedDevice, noteDevice);
    }
    private class SendPostRequest extends AsyncTask<InventoryLab, Void, String> {
        OutputStream outputStream;
        BufferedWriter bufferedWriter;
        JSONObject jsonObjectDevice;
        JSONArray jsonArrayDevice;
        JSONObject postParams;
        @Override
        protected String doInBackground(InventoryLab... params) {
            try {
                // URL url = new URL("https://apiqrcode-v1.herokuapp.com/inventories");
                URL url = new URL(Config.URL+"/inventories/device");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                InventoryLab ivLab = params[0];
                postParams = new JSONObject();
                jsonObjectDevice = new JSONObject();
                    jsonObjectDevice.put("ma_thiet_bi", ivLab.getParentCode());
                    jsonObjectDevice.put("so_luong_thuc_te", ivLab.getNumberOfDeviceLeft());
                    jsonObjectDevice.put("so_luong_thiet_bi_binh_thuong", ivLab.getNumberOfNormalDevice());
                    jsonObjectDevice.put("so_luong_thiet_bi_hong", ivLab.getNumberOfBrokenDevice());
                    jsonObjectDevice.put("so_luong_thiet_bi_thanh_li", ivLab.getNumberOfUnusedDevice());
                    jsonObjectDevice.put("ghi_chu", ivLab.getNoteDevice());

                postParams.put("id_dot",id_dot);
                postParams.put("device", jsonObjectDevice);
                postParams.put("lab_room_id",room_code);
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
            // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

}
