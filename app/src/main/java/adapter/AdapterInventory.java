package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ryne.qr_device_scanner.R;

import java.util.ArrayList;

import model.Device;

/**
 * Created by ryne on 28/03/2017.
 */
public class AdapterInventory extends ArrayAdapter<Device> {
    private ArrayList<Device> arrayInventory;
    private Context context = null;
    private TextView nameDevice;
    private TextView codeParent;
    public AdapterInventory(ArrayList<Device> data, Context context){
        super(context, R.layout.row_item_inventory, data);
        this.arrayInventory = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        Device device = getItem(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_inventory, parent, false);
            nameDevice = (TextView) convertView.findViewById(R.id.nameDeviceInventory);
            codeParent = (TextView) convertView.findViewById(R.id.codeParent);
            result = convertView;
        }else{
            result = convertView;
        }
        nameDevice.setText(device.getName());
        codeParent.setText(device.getParentcode());
      return convertView;
    }
}
