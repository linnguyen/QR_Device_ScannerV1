package com.example.ryne.qr_device_scanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import adapter.AdapterInventory;
import data.JSONDeviceParser;
import model.Device;
import model.Labroom;

public class Inventory extends AppCompatActivity {
    private Context context;
    private Toolbar toolBar;
    private Spinner spinner;
    private ListView listView;
    private FloatingActionButton fabSave;

    private ArrayList<Device> arrlistDevice;
    private ArrayList<Labroom> arrlistLabRoom;
    private AdapterInventory adapterInventory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initToolBar();
      //  initSpinner();
        initListView();
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        arrlistLabRoom = new ArrayList<>();

        // call dattask to load LabRoom from server
        DataTask dataTask = new DataTask();
        dataTask.execute("lab_rooms");
        // process event for FloatActionButton Save
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ArrayList<Labroom> arrLabRoom =  getListViewDataandSendToServer();
                new SendPostRequest().execute(arrLabRoom);

            }
        });
       // new SendPostRequest().execute();
//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                Log.d("idne",Integer.toString(position));
////                Dialog openDialog = new Dialog(Inventory.this);
////                openDialog.setContentView(R.layout.row_dialog);
////                openDialog.setTitle("Device inventory");
////                openDialog.show();
//                Toast.makeText(Inventory.this, "hehe",Toast.LENGTH_LONG).show();
//            }
//        });

    }
    public void initToolBar(){
        toolBar = (Toolbar) findViewById(R.id.toolBarQRSCanner);
        toolBar.setNavigationIcon(R.drawable.left_arrow_small);
        toolBar.setTitle("Select Room");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inventory.this, QRScanner.class);
                startActivity(intent);
            }
        });
    }
    public void initSpinner(){
        spinner = (Spinner) findViewById(R.id.spinner);
        //Device device = new Device("Hehhe","akakka");
       // arrlistLabRoom.add(device);
       // arrlistLabRoom.add(device);
       // arrlistLabRoom.add(device);
        ArrayAdapter<Labroom> arrayAdapter = new ArrayAdapter<Labroom>(
                Inventory.this,
                android.R.layout.simple_list_item_1,
                arrlistLabRoom);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Labroom labroom = (Labroom)parent.getItemAtPosition(position);
                if(labroom.getId() == 1){
                    initListView();
                }else{
                   // initListView1();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initListView(){
        listView = (ListView) findViewById(R.id.listViewInventory);
        arrlistDevice = new ArrayList<>();
        arrlistDevice.add(new Device("Dell Voutro","D001"));
        arrlistDevice.add(new Device("Asus","A001"));
        arrlistDevice.add(new Device("Asus","A001"));
        adapterInventory = new AdapterInventory(arrlistDevice,getApplicationContext());
        listView.setAdapter(adapterInventory);



        // Event ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("idne",Integer.toString(position));
                Dialog openDialog = new Dialog(Inventory.this);
                openDialog.setContentView(R.layout.row_dialog);
                openDialog.setTitle("Device inventory");
                openDialog.show();
                Log.d("TAG","Item 1");

                Toast.makeText(Inventory.this, "hehe",Toast.LENGTH_LONG).show();
            }
        });

    }
    public ArrayList<Labroom> getListViewDataandSendToServer(){
        ArrayList<Labroom> arrLabRoom = new ArrayList<>();
        for(int i=0; i<listView.getCount(); i++){
            View view = listView.getChildAt(i);
            TextView nameDeviceInventory =(TextView) view.findViewById(R.id.nameDeviceInventory);
             EditText edTextBox = (EditText) view.findViewById(R.id.editTextBox);
             Labroom labroom = new Labroom(Integer.parseInt(edTextBox.getText().toString()),nameDeviceInventory.getText().toString());
          //  Log.d("text"+i, String.valueOf(edTextBox.getText()));
             arrLabRoom.add(labroom);

        }
           // adapterInventory.getItem(0)
        return arrLabRoom;

    }
    public class SendPostRequest extends AsyncTask<ArrayList<Labroom>, Void, String>{
        OutputStream outputStream;
        BufferedWriter bufferedWriter;
        JSONObject postParams;
        JSONObject paramsArray;
        JSONArray jsonArray;
        @Override
        protected String doInBackground(ArrayList<Labroom>... params) {
            try {
               // URL url = new URL("https://apiqrcode-v1.herokuapp.com/inventories");
                URL url = new URL("http://10.0.3.2:3000/inventories");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                ArrayList<Labroom> arrayListLabroom = params[0];
                paramsArray = new JSONObject();
                jsonArray = new JSONArray();
              //  Log.d("params", Integer.toString(arrayListLabroom.size()));
                for(int i=0; i< arrayListLabroom.size();i++) {
                    postParams = new JSONObject();

                    // postParams.put("amount_real",arrayListLabroom.get(i).getId());
                    postParams.put("date_of_inventory", arrayListLabroom.get(i).getName());
                    jsonArray.put(postParams);
                    // postParams.put("id_inventory", arrayListLabroom.get(0).getId());
                }
                   paramsArray.put("arraydevice",jsonArray);
                   outputStream = connection.getOutputStream();
                   bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                   bufferedWriter.write(String.valueOf(paramsArray));

                   Log.d("params", String.valueOf(postParams));

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
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
    private class DataTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String data = HttpHandler.makeServiceCall(params[0]);
            return data;
        }
        @Override
        protected void onPostExecute(String s) {
            arrlistLabRoom = JSONDeviceParser.getLabRoomData(s);
            initSpinner();
        }
    }
}
