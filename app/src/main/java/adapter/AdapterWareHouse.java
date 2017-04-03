package adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ryne.qr_device_scanner.R;

import java.util.ArrayList;
import java.util.Locale;

import model.Device;

/**
 * Created by ryne on 03/04/2017.
 */

public class AdapterWareHouse extends ArrayAdapter<Device> {
    private ArrayList<Device> arraySearch;
    private ArrayList<Device> arrayInventory;

    private Context context = null;
    private TextView nameDevice;
    public AdapterWareHouse(ArrayList<Device> data, Context context){
        super(context, R.layout.row_item_inventory, data);
        this.arrayInventory = data;
        this.context = context;
        arraySearch = new ArrayList<>();
        arraySearch.addAll(arrayInventory);
    }

    @Override
    public int getCount() {
        return arrayInventory.size();
    }

    @Nullable
    @Override
    public Device getItem(int position) {
        return arrayInventory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        Device device = getItem(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_ware_house, parent, false);
            nameDevice = (TextView) convertView.findViewById(R.id.nameDeviceInventory);
            result = convertView;
        }else{
            result = convertView;
        }

        nameDevice.setText(device.getName());
        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        arrayInventory.clear();
        if(charText.length() == 0){
            arrayInventory.addAll(arraySearch);
        }else{
            for(Device device: arraySearch){
                if(device.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    arrayInventory.add(device);
                }
            }
        }
        notifyDataSetChanged();
    }
}

