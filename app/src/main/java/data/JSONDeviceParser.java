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
            String name = jsonObject.getString("nameofdevice");
            device = new Device(name, "", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return device;
    }
}
