package com.example.ryne.qr_device_scanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import Utils.Config;
import adapter.AdapterInventory;
import data.HttpHandler;
import data.JSONDeviceParser;
import model.Device;
import model.InventoryLab;
import model.Labroom;

public class ActivityInventory extends AppCompatActivity {
    private Context context;
    private Toolbar toolBar;
    private Spinner spinner;
    private ListView listView;
    private FloatingActionButton fabSave;
    private TextView codeParent;
    private EditText editTextBox;
    private EditText noteDeviceSave;
    private EditText numberofDeviceLeft;
    private EditText noteDevice;

    private ArrayList<Device> arrlistDevice;
    private ArrayList<Labroom> arrlistLabRoom;
    private AdapterInventory adapterInventory;
    private Labroom labroom;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initToolBar();
      //  initSpinner();
      //  initListView();
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        arrlistLabRoom = new ArrayList<>();
        arrlistDevice = new ArrayList<>();
        // call dattask to load LabRoom from server
        DataTaskLabRoom dataTaskLabRoom = new DataTaskLabRoom();
        dataTaskLabRoom.execute("/lab_rooms");
        // process event for FloatActionButton Save
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityInventory.this);
                builder.setMessage("You must check your input carefully before submitting.\nAre you sure?")
                        .setTitle("ActivityInventory Submit")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                  ArrayList<InventoryLab> arrLabRoom =  getListViewData();
                                  new SendPostRequest().execute(arrLabRoom);
                                 // hide fabbutton not to allow user submit data second time
                                 // fabSave.hide();
                                // Inform success for user
                                 final Dialog openDialog = new Dialog(ActivityInventory.this);
                                 openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                 openDialog.setContentView(R.layout.dialog_inventory_success);
                                 openDialog.show();
                                 final Button dialogButtonSuccess = (Button)openDialog.findViewById(R.id.daButtonSuccess);
                                 dialogButtonSuccess.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         openDialog.dismiss();
                                     }
                                 });

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }
    public void initToolBar(){
        toolBar = (Toolbar) findViewById(R.id.toolBarQRSCanner);
        toolBar.setNavigationIcon(R.drawable.left_arrow_white);
        //toolBar.setTitle("Select Room");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityInventory.this, ActivityQRScanner.class);
                startActivity(intent);
            }
        });
    }
    public void initSpinner(){
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<Labroom> arrayAdapter = new ArrayAdapter<Labroom>(
                ActivityInventory.this,
                android.R.layout.simple_list_item_1,
                arrlistLabRoom);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labroom = (Labroom)parent.getItemAtPosition(position);
                DataTaskDevices dataTaskDevices = new DataTaskDevices();
                dataTaskDevices.execute("/devices/"+labroom.getId());
                fabSave.show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initListView(){
        listView = (ListView) findViewById(R.id.listViewInventory);
        // arrlistDevice = new ArrayList<>();
//        arrlistDevice.add(new Device("Dell Voutro","D001"));
//        arrlistDevice.add(new Device("Dell Voutro","D001"));
//        arrlistDevice.add(new Device("Dell Voutro","D001"));
        adapterInventory = new AdapterInventory(arrlistDevice,getApplicationContext());
        listView.setAdapter(adapterInventory);
        // event ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view = listView.getChildAt(position);
                editTextBox = (EditText) view.findViewById(R.id.editTextBox);
                noteDeviceSave = (EditText) view.findViewById(R.id.noteDeviceSave);
                // dialogbox for each row
                final Dialog openDialog = new Dialog(ActivityInventory.this);
                //openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                openDialog.setContentView(R.layout.dialog_row_inventory);
                openDialog.setTitle("ActivityInventory Input");
                openDialog.show();
                numberofDeviceLeft = (EditText) openDialog.findViewById(R.id.numberofDeviceLeft);
                noteDevice = (EditText) openDialog.findViewById(R.id.noteDevice);
                Button diologButton = (Button) openDialog.findViewById(R.id.daButtonOK);
                diologButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String parentCode = getListViewData(position);
                          String numberOfDeviceLeft = numberofDeviceLeft.getText().toString();
                          String noteDevice = ActivityInventory.this.noteDevice.getText().toString();
                          editTextBox.setText(numberOfDeviceLeft);
                          noteDeviceSave.setText(noteDevice);
                          openDialog.dismiss();
                    }
                });
            }
        });
    }
   // this function is redundant
    public String getListViewData(int position){
        HashMap<String, String> hashMapData = new HashMap<String, String>();
        View view = listView.getChildAt(position);
        TextView nameDeviceInventory = (TextView) view.findViewById(R.id.nameDeviceInventory);
        TextView codeParent = (TextView) view.findViewById(R.id.codeParent);
       // EditText edTextBox = (EditText) view.findViewById(R.id.editTextBox);
        hashMapData.put("name_device", nameDeviceInventory.getText().toString());
        hashMapData.put("code_parent", codeParent.getText().toString());
        return codeParent.getText().toString();
    }
    // this function for test
    public ArrayList<InventoryLab> getListViewData(){
        ArrayList<InventoryLab> arrayLabRoom =new ArrayList<>();
        InventoryLab inventoryLab = null;
        for (int i=0; i< listView.getCount(); i++){
            View view = listView.getChildAt(i);
            codeParent =(TextView) view.findViewById(R.id.codeParent);
            //Log.d("codeparent", codeParent.getText().toString());
            editTextBox = (EditText) view.findViewById(R.id.editTextBox);
            noteDeviceSave = (EditText) view.findViewById(R.id.noteDeviceSave);
            if (editTextBox.getText().toString().equals("") && codeParent.getText()!=null) {
                 continue;
            }else{
                inventoryLab = new InventoryLab(codeParent.getText().toString(),
                        Integer.parseInt(editTextBox.getText().toString()),
                        noteDeviceSave.getText().toString());
            }
            arrayLabRoom.add(inventoryLab);
        }
       // Log.d("arrayne", arrayLabRoom.get(1).getParentCode());
        return arrayLabRoom;
    }
    public class SendPostRequest extends AsyncTask<ArrayList<InventoryLab>, Void, String>{
        OutputStream outputStream;
        BufferedWriter bufferedWriter;
        JSONObject jsonObjectDevice;
        JSONArray jsonArrayDevice;
        JSONObject postParams;
        @Override
        protected String doInBackground(ArrayList<InventoryLab>... params) {
            try {
                // URL url = new URL("https://apiqrcode-v1.herokuapp.com/inventories");
                URL url = new URL(Config.URL+"/inventories");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                ArrayList<InventoryLab> arrayLab = params[0];
                postParams = new JSONObject();
                jsonArrayDevice = new JSONArray();
                for(int i=0; i< arrayLab.size(); i++) {
                    jsonObjectDevice = new JSONObject();
                    jsonObjectDevice.put("ma_thiet_bi", arrayLab.get(i).getParentCode());
                    jsonObjectDevice.put("so_luong_thuc_te", arrayLab.get(i).getNumberOfDeviceLeft());
                    jsonObjectDevice.put("ghi_chu", arrayLab.get(i).getNoteDevice());
                    jsonArrayDevice.put(jsonObjectDevice);
                }
                postParams.put("array_of_device", jsonArrayDevice);
                postParams.put("lab_room_id", labroom.getId());
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

    private class DataTaskLabRoom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String dataLabRoom = HttpHandler.makeServiceCall(params[0]);
            Log.d("dulieu", dataLabRoom);
            return dataLabRoom;
        }
        @Override
        protected void onPostExecute(String dataLabRoom) {
            arrlistLabRoom = JSONDeviceParser.getLabRoomData(dataLabRoom);
            initSpinner();
        }
    }
    private  class DataTaskDevices extends AsyncTask<String, Void, String> {
        private ProgressBar progressBar;
        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.pgBar);
            progressBar.setVisibility(ProgressBar.VISIBLE);
//            progressDialog = new ProgressDialog(ActivityInventory.this);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setTitle("Please wait");
//            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String dataDevices = HttpHandler.makeServiceCall(params[0]);
            //Log.d("thietbvi",dataDevices);
            return dataDevices;
        }
        @Override
        protected void onPostExecute(String jsonString) {
            progressBar.setVisibility(ProgressBar.GONE);
            arrlistDevice = JSONDeviceParser.getOutputDevice(jsonString);
            initListView();
        }
    }
}
