package com.example.ryne.qr_device_scanner;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Utils.Config;

/**
 * Created by lin on 26/12/2016.
 */

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){
    }
    public static String makeServiceCall(String extendUrl){
        String response = null;
        try {
            URL url = new URL(Config.URL+extendUrl);
            Log.d("URL",Config.URL + extendUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // read the response
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            response = covertStreamToString(inputStream);
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return response;
    }

    private static String covertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try{
         while((line = reader.readLine())!= null){
            stringBuilder.append(line).append('\n');
        }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return stringBuilder.toString();
    }
}
