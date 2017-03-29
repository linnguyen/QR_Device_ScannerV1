package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Device;
import model.Labroom;

/**
 * Created by ryne on 22/03/2017.
 */

public class JSONDeviceParser {
    public static Device getDeviceData(String jsonString){
        Device device = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            //String name = jsonObject.getString("nameofdevice");
            JSONObject jsonObjectStaff = jsonObject.getJSONObject("staff");
            String staff = jsonObjectStaff.getString("name");

            JSONObject jsonObjectDevice = jsonObject.getJSONObject("device");
            String name = jsonObjectDevice.getString("name");
            String parentCode =  jsonObjectDevice.getString("qrcode");
            String producer = jsonObjectDevice.getString("producer");
            String country  = jsonObjectDevice.getString("country");
            String dateofProduce = jsonObjectDevice.getString("date_of_produce");
            String digital = jsonObjectDevice.getString("digital");
            String timeofWarranty = jsonObjectDevice.getString("time_of_warranty");
            String description = jsonObjectDevice.getString("description");

            device = new Device(name, parentCode, producer, country, dateofProduce, digital, staff, "", timeofWarranty, description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return device;
    }
    public static ArrayList<Labroom> getLabRoomData(String jsonString) {
        ArrayList<Labroom> arrLabRoom = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray arrayLabRoomJson = jsonObject.getJSONArray("labrooms");
            for (int i=0; i < arrayLabRoomJson.length(); i++){
               JSONObject objectLabRoomJson = arrayLabRoomJson.getJSONObject(i);
                int id = objectLabRoomJson.getInt("id");
                String name = objectLabRoomJson.getString("name");
                Labroom labroom = new Labroom(id,name);
                arrLabRoom.add(labroom);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrLabRoom;
    }
}
