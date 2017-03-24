package data;

import org.json.JSONException;
import org.json.JSONObject;

import model.Device;

/**
 * Created by ryne on 22/03/2017.
 */

public class JSONDeviceParser {
    public Device getDeviceData(String jsonString){
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
}
