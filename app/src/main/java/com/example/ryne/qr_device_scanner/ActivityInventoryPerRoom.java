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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import utils.Config;
import adapter.AdapterInventory;
import data.HttpHandler;
import data.JSONDeviceParser;
import model.Device;
import model.InventoryLab;
import model.Labroom;

public class ActivityInventoryPerRoom extends AppCompatActivity {
    private Context context;
    private Toolbar toolBar;
    private Spinner spinner;
    private ListView listView;
    private FloatingActionButton fabSave;
    private TextView tvCodeParent;
    private EditText edShowRowStatus;
    private EditText edNoteDevice;
    private EditText edNumberOfDeviceLeft;
    private EditText edNumberOfNormalDevice;
    private EditText edNumberOfBrokenDevice;
    private EditText edNumberOfUnusedDevice;
    private EditText edNumberOfDeviceLeftSave;
    private EditText edNumberOfNormalDeviceSave;
    private EditText edNumberOfBrokenDeviceSave;
    private EditText edNumberOfUnusedDeviceSave;
    private EditText edNoteDeviceSave;
    private ProgressBar progressBar;

    private ArrayList<Device> arrlistDevice;
    private ArrayList<Labroom> arrlistLabRoom;
    private AdapterInventory adapterInventory;
    private Labroom labroom;
    private ArrayList<InventoryLab> arrLabRoom;
    private int id_dot;
    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initToolBar();
      //  initSpinner();
      //  initListView();
        listView = (ListView) findViewById(R.id.listViewInventory);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        arrlistLabRoom = new ArrayList<>();
        arrlistDevice = new ArrayList<>();
        arrLabRoom = new ArrayList<>();
        // call dattask to load LabRoom from server
        DataTaskLabRoom dataTaskLabRoom = new DataTaskLabRoom();
        dataTaskLabRoom.execute("/lab_rooms");
        // receive id_dot from activity inventory_season
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            id_dot = intent.getExtras().getInt("id_dot");
        }
        // process event for FloatActionButton Save
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrLabRoom = getListViewData();
                Log.d("dulieuday", arrLabRoom.toString());
                if (arrLabRoom.isEmpty()) {
                    final Dialog openDialog = new Dialog(ActivityInventoryPerRoom.this);
                    openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    openDialog.setContentView(R.layout.dialog_inventory_fail);
                    openDialog.show();
//                    Toast.makeText(ActivityInventoryPerRoom.this, "Bạn chưa nhập thông tin kiểm kê. Vui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityInventoryPerRoom.this);
                    builder.setMessage("Bạn phải kiểm tra thông tin nhập của bạn cẩn thận trước khi gửi.\nBạn có chắc chắn không?")
                            .setTitle("Gửi Thông Tin Kiểm Kê")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new SendPostRequest().execute(arrLabRoom);
                                    // hide fabbutton not to allow user submit data second time
                                    fabSave.hide();
                                    // Inform success for user
                                    final Dialog openDialog = new Dialog(ActivityInventoryPerRoom.this);
                                    openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    openDialog.setContentView(R.layout.dialog_inventory_success_per_room);
                                    openDialog.show();
                                    final Button dialogButtonSuccess = (Button) openDialog.findViewById(R.id.daButtonSuccess);
                                    dialogButtonSuccess.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            listView.setEnabled(false);
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
                Intent intent = new Intent(ActivityInventoryPerRoom.this, ActivityQRScanner.class);
                startActivity(intent);
            }
        });
    }
    public void initSpinner(){
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<Labroom> arrayAdapter = new ArrayAdapter<Labroom>(
                ActivityInventoryPerRoom.this,
                R.layout.spinner_item,
                arrlistLabRoom);
//        ArrayAdapter<Labroom> arrayAdapter = ArrayAdapter.createFromResource(this,arrlistLabRoom, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labroom = (Labroom)parent.getItemAtPosition(position);
                CheckLatestInventoryRequest checkLatestInventoryRequest = new CheckLatestInventoryRequest();
                checkLatestInventoryRequest.execute(labroom.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initListView(){
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
                edShowRowStatus = (EditText) view.findViewById(R.id.edShowRowStatus);
                edNumberOfDeviceLeftSave = (EditText) view.findViewById(R.id.edNumberOfDeviceLeftSave);
                edNumberOfNormalDeviceSave = (EditText) view.findViewById(R.id.edNumberOfNormalDeviceSave);
                edNumberOfBrokenDeviceSave = (EditText) view.findViewById(R.id.edNumberOfBrokenDeviceSave);
                edNumberOfUnusedDeviceSave = (EditText) view.findViewById(R.id.edNumberOfUnusedDeviceSave);
                edNoteDeviceSave = (EditText) view.findViewById(R.id.edNoteDeviceSave);

                // dialogbox for each row
                final Dialog openDialog = new Dialog(ActivityInventoryPerRoom.this);
//                Window window = openDialog.getWindow();
//                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                //openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                openDialog.setContentView(R.layout.dialog_row_inventory);
                openDialog.setTitle("Thông Tin Kiểm Kê Thiết Bị");
                openDialog.show();
                edNumberOfDeviceLeft = (EditText) openDialog.findViewById(R.id.edNumberOfDeviceLeft);
                edNumberOfNormalDevice = (EditText) openDialog.findViewById(R.id.edNumberOfNormalDevice);
                edNumberOfBrokenDevice = (EditText) openDialog.findViewById(R.id.edNumberOfBrokenDevice);
                edNumberOfUnusedDevice = (EditText) openDialog.findViewById(R.id.edNumberOfUnusedDevice);
                edNoteDevice = edNoteDevice = (EditText) openDialog.findViewById(R.id.edNoteDevice);

                // incase user click row item at second time
                edNumberOfDeviceLeft.setText(edNumberOfDeviceLeftSave.getText().toString());
                edNumberOfNormalDevice.setText(edNumberOfNormalDeviceSave.getText().toString());
                edNumberOfBrokenDevice.setText(edNumberOfBrokenDeviceSave.getText().toString());
                edNumberOfUnusedDevice.setText(edNumberOfUnusedDeviceSave.getText().toString());
                edNoteDevice.setText(edNoteDeviceSave.getText().toString());

                Button diologButton = (Button) openDialog.findViewById(R.id.daButtonOK);
                diologButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         //String parentCode = getListViewData(position);
                          String numberOfDeviceLeft = edNumberOfDeviceLeft.getText().toString();
                          String numberOfNormalDevice = edNumberOfNormalDevice.getText().toString();
                          String numberOfBorkenDevice = edNumberOfBrokenDevice.getText().toString();
                          String numberOfUnusedDevice = edNumberOfUnusedDevice.getText().toString();
                          String noteDevice = ActivityInventoryPerRoom.this.edNoteDevice.getText().toString();
                          edNoteDeviceSave.setText(noteDevice);
                          edNumberOfDeviceLeftSave.setText(numberOfDeviceLeft);
                          edNumberOfNormalDeviceSave.setText(numberOfNormalDevice);
                          edNumberOfBrokenDeviceSave.setText(numberOfBorkenDevice);
                          edNumberOfUnusedDeviceSave.setText(numberOfUnusedDevice);
                          if(checkInputInventory()) {
                              int tick = R.drawable.tick;
                              edShowRowStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, tick);
                          }
                          openDialog.dismiss();
                    }
                });
            }
        });
    }

    public boolean checkInputInventory(){
        if(edNumberOfDeviceLeft.getText().toString().trim().length()>0
         || edNumberOfNormalDevice.getText().toString().trim().length()>0
         || edNumberOfBrokenDevice.getText().toString().trim().length()>0
         || edNumberOfUnusedDevice.getText().toString().trim().length()>0
         || edNoteDevice.getText().toString().trim().length()>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkInputInventorySave(){
        if(edNumberOfDeviceLeftSave.getText().toString().trim().length()==0
                && edNumberOfNormalDeviceSave.getText().toString().trim().length()==0
                && edNumberOfBrokenDeviceSave.getText().toString().trim().length()==0
                && edNumberOfUnusedDeviceSave.getText().toString().trim().length()==0
                && edNoteDeviceSave.getText().toString().trim().length()==0){
            return true;
        }else{
            return false;
        }
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
    public ArrayList<InventoryLab> getListViewData() {
        ArrayList<InventoryLab> arrayLabRoom = new ArrayList<>();
        InventoryLab inventoryLab = null;
        for (int i = 0; i < listView.getCount(); i++) {
            View view = listView.getChildAt(i);
            tvCodeParent = (TextView) view.findViewById(R.id.tvCodeParent);
            //Log.d("codeparent", codeParent.getText().toString());
            edNoteDeviceSave = (EditText) view.findViewById(R.id.edNoteDeviceSave);
            edNumberOfDeviceLeftSave = (EditText) view.findViewById(R.id.edNumberOfDeviceLeftSave);
            edNumberOfNormalDeviceSave = (EditText) view.findViewById(R.id.edNumberOfNormalDeviceSave);
            edNumberOfBrokenDeviceSave = (EditText) view.findViewById(R.id.edNumberOfBrokenDeviceSave);
            edNumberOfUnusedDeviceSave = (EditText) view.findViewById(R.id.edNumberOfUnusedDeviceSave);
            if (checkInputInventorySave()) {
                continue;
            } else {
                int numberOfDeviceLeft = -1;
                int numberOfNormalDevice = -1;
                int numberOfBrokenDevice = -1;
                int numberOfUnusedDevice = -1;
                if (edNumberOfDeviceLeftSave.getText().toString().trim().length() > 0) {
                    numberOfDeviceLeft = Integer.parseInt(edNumberOfDeviceLeftSave.getText().toString());
                }
                if (edNumberOfNormalDeviceSave.getText().toString().trim().length() > 0) {
                    numberOfNormalDevice = Integer.parseInt(edNumberOfNormalDeviceSave.getText().toString());
                }
                if (edNumberOfBrokenDeviceSave.getText().toString().trim().length() > 0) {
                    numberOfBrokenDevice = Integer.parseInt(edNumberOfBrokenDeviceSave.getText().toString());
                }
                if (edNumberOfUnusedDeviceSave.getText().toString().trim().length() > 0) {
                    numberOfUnusedDevice = Integer.parseInt(edNumberOfUnusedDeviceSave.getText().toString());
                }
                inventoryLab = new InventoryLab(
                        tvCodeParent.getText().toString(),
                        numberOfDeviceLeft,
                        numberOfNormalDevice,
                        numberOfBrokenDevice,
                        numberOfUnusedDevice,
                        edNoteDeviceSave.getText().toString());
                arrayLabRoom.add(inventoryLab);
            }

        }
        return arrayLabRoom;
    }
    private class SendPostRequest extends AsyncTask<ArrayList<InventoryLab>, Void, String>{
        OutputStream outputStream;
        BufferedWriter bufferedWriter;
        JSONObject jsonObjectDevice;
        JSONArray jsonArrayDevice;
        JSONObject postParams;
        @Override
        protected String doInBackground(ArrayList<InventoryLab>... params) {
            try {
                // URL url = new URL("https://apiqrcode-v1.herokuapp.com/inventories");
                URL url = new URL(Config.URL+"/inventories/room");
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
                    jsonObjectDevice.put("so_luong_thiet_bi_binh_thuong", arrayLab.get(i).getNumberOfNormalDevice());
                    jsonObjectDevice.put("so_luong_thiet_bi_hong",arrayLab.get(i).getNumberOfBrokenDevice());
                    jsonObjectDevice.put("so_luong_thiet_bi_thanh_li",arrayLab.get(i).getNumberOfUnusedDevice());
                    jsonObjectDevice.put("ghi_chu",arrayLab.get(i).getNoteDevice());
                    jsonArrayDevice.put(jsonObjectDevice);
                }
                postParams.put("id_dot",id_dot);
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
//           Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
         Log.d("daata",JSONDeviceParser.getMessageResponse(s));
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
        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.pgBar);
            progressBar.setVisibility(ProgressBar.VISIBLE);
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
                URL url = new URL(Config.URL + "/inventories/latest_inventory_for_room");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                String maPth = params[0];
                postParams = new JSONObject();
                postParams.put("ma_pth", maPth);
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
            if (message == "") {
                showOutputDevice();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityInventoryPerRoom.this);
                builder.setMessage(message)
                        .setTitle("Xác Nhận")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               showOutputDevice();
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
        public void showOutputDevice(){
            DataTaskDevices dataTaskDevices = new DataTaskDevices();
            dataTaskDevices.execute("/devices/"+labroom.getId());
            listView.setEnabled(true);
            fabSave.show();
        }
    }

}
