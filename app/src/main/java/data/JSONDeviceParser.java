package data;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Device;
import model.InventorySeason;
import model.Labroom;

/**
 * Created by ryne on 22/03/2017.
 */

public class JSONDeviceParser {
    public static Device getDeviceData(String jsonString){
        Device device = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObjectStaff = jsonObject.getJSONObject("staff");
            String staff = jsonObjectStaff.getString("ho_ten");
            String room = jsonObjectStaff.getString("ma_pth");

            JSONObject jsonObjectDevice = jsonObject.getJSONObject("device");
            String name = jsonObjectDevice.getString("ten_thiet_bi");
            String parentCode =  jsonObjectDevice.getString("ma_thiet_bi");
            String producer = jsonObjectDevice.getString("hang_san_xuat");
            String dateofProduce = jsonObjectDevice.getString("ngay_san_xuat");
            String digital = jsonObjectDevice.getString("thong_so_ki_thuat");
            String timeofWarranty = jsonObjectDevice.getString("han_bao_hanh");
            String description = jsonObjectDevice.getString("mo_ta");

            JSONObject jsonObjectOrigin = jsonObject.getJSONObject("origin");
            String country  = jsonObjectOrigin.getString("nuoc_san_xuat");

            device = new Device(name, parentCode, producer, country, dateofProduce, digital, staff, room, timeofWarranty, description);
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
                String id = objectLabRoomJson.getString("ma_pth");
                String name = objectLabRoomJson.getString("phong_thuc_hanh");
                Labroom labroom = new Labroom(id,name);
                arrLabRoom.add(labroom);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrLabRoom;
    }
    public static ArrayList<Device> getOutputDevice(String jsonString){
        ArrayList<Device> arrDevice = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray arrDeviceJSon = jsonObject.getJSONArray("devices");
            for (int i=0; i<arrDeviceJSon.length(); i++){
                JSONObject objectDevice = arrDeviceJSon.getJSONObject(i);
                String name = objectDevice.getString("ten_thiet_bi");
                String code_of_parent = objectDevice.getString("ma_thiet_bi");
                Device device = new Device(name, code_of_parent);
                arrDevice.add(device);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrDevice;
    }
    public static ArrayList<InventorySeason> getInventorySeason(String jsonString) {
        ArrayList<InventorySeason> arrIvSeason = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray arrIvSeasonJSon = jsonObject.getJSONArray("inventory_season");
            for(int i=0; i< arrIvSeasonJSon.length(); i++){
                JSONObject objectIvSeason = arrIvSeasonJSon.getJSONObject(i);
                int id = objectIvSeason.getInt("id_dot");
                String name = objectIvSeason.getString("ten");
                InventorySeason inventorySeason = new InventorySeason(id, name);
                arrIvSeason.add(inventorySeason);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrIvSeason;
    }
    public static String getMessageResponse(String jsonString) {
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            message = jsonObject.getString("info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
