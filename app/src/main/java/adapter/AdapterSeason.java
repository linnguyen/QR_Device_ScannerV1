package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ryne.qr_device_scanner.R;

import java.util.ArrayList;

import model.Device;


/**
 * Created by ryne on 26/04/2017.
 */

public class AdapterSeason extends ArrayAdapter<String>{
    private ArrayList<String> arrSeason;
    private Context context;
    private RadioButton rbSeason;
    public AdapterSeason(ArrayList<String> data, Context context){
        super(context, R.layout.row_item_season, data);
        this.arrSeason = data;
        this.context = context;
    }
    public View geView(int position, View convertView, ViewGroup viewGroup){
        View view;
        String season = getItem(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_inventory, viewGroup, false);
            rbSeason = (RadioButton) convertView.findViewById(R.id.rbSeason);
            view = convertView;
        }else{
            view = convertView;
        }
        rbSeason.setText(season);
        return convertView;
    }
}
