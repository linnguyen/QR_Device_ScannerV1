package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ryne.qr_device_scanner.R;

import java.util.ArrayList;

import model.Device;


/**
 * Created by ryne on 26/04/2017.
 */

public class AdapterSeason extends BaseAdapter{
    LayoutInflater inflater;
    ArrayList<String> data;

    public AdapterSeason(Context context, ArrayList<String> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if (v == null) {
            v = inflater.inflate(R.layout.row_item_season, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
            return null;
        }else{
            holder = (ViewHolder) v.getTag();
        }
        holder.radioGroup.setTag(new Integer(position));

        return v;
    }

    class ViewHolder {
        RadioGroup radioGroup;
        ViewHolder(View v){
            radioGroup = (RadioGroup) v.findViewById(R.id.rbgSeason);
        }
    }
}
