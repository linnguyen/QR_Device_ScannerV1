package com.example.ryne.qr_device_scanner;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import data.JSONDeviceParser;
import model.Device;

public class QRScanner extends AppCompatActivity {
    private Button btScan;

    private JSONDeviceParser jsonDeviceParser = new JSONDeviceParser();
    private Device device = new Device();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.startActivity(new Intent(QRScanner.this,DeviceInformation.class));
        btScan = (Button) findViewById(R.id.btClick);
        final Activity activity = this;

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                mediaPlayer.start();
                DataTask dataTask = new DataTask();
                dataTask.execute("D01");
                Intent intent = new Intent(QRScanner.this, DeviceInformation.class);
                intent.putExtra("objDevice", device);
                startActivity(intent);
            }else{
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                mediaPlayer.start();
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                DataTask dataTask = new DataTask();
                dataTask.execute(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private class DataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String data = HttpHandler.makeServiceCall(params[0]);
            return data;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            Toast.makeText(QRScanner.this,jsonString,Toast.LENGTH_LONG).show();
            device = jsonDeviceParser.getDeviceData(jsonString);
        }
    }

}
