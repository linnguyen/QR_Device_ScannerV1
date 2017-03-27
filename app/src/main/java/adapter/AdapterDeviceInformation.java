package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ryne.qr_device_scanner.R;

import java.util.List;

import model.Device;

/**
 * Created by ryne on 27/03/2017.
 */

public class AdapterDeviceInformation extends BaseAdapter {
    private List<Device> listItems;
    private LayoutInflater layoutInflater;
    private Context context;
    private TextView textView;

    public AdapterDeviceInformation(Context context, List<Device> listItems){
        this.context = context;
        this.listItems = listItems;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (listItems.isEmpty() || listItems == null) {
            return 0;
        }
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        Device device = (Device)getItem(i);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.row_item, null);
            holder = new ViewHolder();

            holder.name = (TextView) view.findViewById(R.id.nameDevice);
            holder.parentCode = (TextView) view.findViewById(R.id.parentCode);
            holder.origin= (TextView) view.findViewById(R.id.origin);
            holder.dateOfProduce = (TextView) view.findViewById(R.id.dateOfProduce);
            holder.staff = (TextView) view.findViewById(R.id.staff);
            holder.digital = (TextView) view.findViewById(R.id.digital);


            view.setTag(textView);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(device.getName());
        holder.parentCode.setText(device.getParentcode());
        holder.origin.setText(device.getCountry());
        holder.dateOfProduce.setText(device.getDateofProduce());
        holder.staff.setText(device.getStaff());
        holder.digital.setText(device.getDescription());
        return view;
    }

    public static class ViewHolder {
        TextView name;
        TextView parentCode;
        TextView origin;
        TextView dateOfProduce;
        TextView staff;
        TextView digital;
    }
}
