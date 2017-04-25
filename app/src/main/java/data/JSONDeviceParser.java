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
            JSONObject jsonObjectStaff = jsonObject.getJSONObject("staff");
            String staff = jsonObjectStaff.getString("ho_ten");

            JSONObject jsonObjectDevice = jsonObject.getJSONObject("device");
            String name = jsonObjectDevice.getString("ten_thiet_bi");
            String parentCode =  jsonObjectDevice.getString("ma_code");
            String producer = jsonObjectDevice.getString("hang_san_xuat");
            String country  = jsonObjectDevice.getString("ma_nuoc_san_xuat");
            String dateofProduce = jsonObjectDevice.getString("ngay_san_xuat");
            String digital = jsonObjectDevice.getString("thong_so_ki_thuat");
            String timeofWarranty = jsonObjectDevice.getString("han_bao_hanh");
            String description = jsonObjectDevice.getString("mo_ta");

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
}
