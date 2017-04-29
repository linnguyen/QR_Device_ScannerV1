package com.example.ryne.qr_device_scanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    private int id_dot;
    private String room_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_per_device);
        initToolBar();
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            id_dot = intent.getExtras().getInt("id_dot");
        }
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
                URL url = new URL(Config.URL+"/inventories");
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
                postParams.put("array_of_device", jsonArrayDevice);
               // postParams.put("lab_room_id", );
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
