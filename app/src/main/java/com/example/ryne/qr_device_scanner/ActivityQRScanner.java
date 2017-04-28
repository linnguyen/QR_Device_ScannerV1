package com.example.ryne.qr_device_scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

import data.HttpHandler;
import data.JSONDeviceParser;
import model.Device;

public class ActivityQRScanner extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private Toolbar toolBar;
    private Button btScan;
    private ImageView imQrSCanner;
    private ImageView imInventory;
    private ImageView imWareHouse;
    private SliderLayout imageSlider;

    private JSONDeviceParser jsonDeviceParser = new JSONDeviceParser();
    private Device device = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // this.startActivity(new Intent(this,ActivityInventory.class));
        initToolBar();
        initSlider();
        final Activity activity = this;
        imQrSCanner = (ImageView)findViewById(R.id.qrScanner);
        imInventory = (ImageView)findViewById(R.id.inventory);
        imWareHouse = (ImageView)findViewById(R.id.wareHouse);
        imQrSCanner.setOnClickListener(new View.OnClickListener() {
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
        imInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("labroomne", "tired");
                Intent intent = new Intent(ActivityQRScanner.this, ActivityInventory.class);
                startActivity(intent);
            }
        });
        imWareHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityQRScanner.this, ActivityWareHouse.class);
                startActivity(intent);
            }
        });

    }
    public void initToolBar(){
        toolBar = (Toolbar) findViewById(R.id.toolBarQRSCanner);
        toolBar.setNavigationIcon(R.drawable.qrcode);
        toolBar.setTitle("Device Scanner");
    }
    public void initSlider(){
        imageSlider = (SliderLayout) findViewById(R.id.sliderImage);
        HashMap<String, Integer> file_maps = new HashMap<>();
        file_maps.put("Welcome to DN Vocational College", R.drawable.caodangnghe1);
        file_maps.put("Device Management", R.drawable.caodangnghe2);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView.description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            imageSlider.addSlider(textSliderView);
        }
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        imageSlider.setDuration(2000);
        imageSlider.addOnPageChangeListener(this);
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
                dataTask.execute("/device_informations/US2017MTM00011");
            }else{
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                mediaPlayer.start();
               // Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                DataTask dataTask = new DataTask();
                dataTask.execute("/device_informations/"+result.getContents());
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
           // Toast.makeText(ActivityQRScanner.this,jsonString,Toast.LENGTH_LONG).show()
            //create device object
            device = jsonDeviceParser.getDeviceData(jsonString);
            //pass to ActivityDeviceInformation activity
            Intent intent = new Intent(ActivityQRScanner.this, ActivityDeviceInformation.class);
            intent.putExtra("objDevice", device);
            startActivity(intent);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    protected void onStop() {
        imageSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ActivityQRScanner.this, "Permission denied on this device!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }



}
