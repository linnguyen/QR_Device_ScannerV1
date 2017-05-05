package com.example.ryne.qr_device_scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class Test extends AppCompatActivity {

    private ArrayList<String> arrSeason = null;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }

}
