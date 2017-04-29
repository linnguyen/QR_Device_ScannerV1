package adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ryne.qr_device_scanner.R;

import java.util.ArrayList;

import model.InventorySeason;

/**
 * Created by ryne on 03/04/2017.
 */

public class AdapterSeason extends ArrayAdapter<InventorySeason> {
    private ArrayList<InventorySeason> arrSeason;
    private int selected_position = -1;

    private Context context = null;
    private TextView nameSeason;
    private CheckBox cbSeasonSelect;
    private EditText edIdSeasonSelect;
    public AdapterSeason(ArrayList<InventorySeason> data, Context context){
        super(context, R.layout.row_item_season, data);
        this.arrSeason = data;
        this.context = context;;
    }
    @Override
    public int getCount() {
        return arrSeason.size();
    }

    @Nullable
    @Override
    public InventorySeason getItem(int position) {
        return arrSeason.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View result;
        InventorySeason season = getItem(position);
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_season, parent, false);
            nameSeason = (TextView) convertView.findViewById(R.id.tvSeason);
            cbSeasonSelect = (CheckBox) convertView.findViewById(R.id.cbSeasonSelect);
            edIdSeasonSelect = (EditText) convertView.findViewById(R.id.edIdSeasonSelect);
//            // for change selecting
//            cbSeasonSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked){
//                        selected_position = position;
//                    }else{
//                        selected_position = -1;
//                    }
//                    notifyDataSetChanged();
//                }
//            });

 //           cbSeasonSelect.setChecked(selected_position == position);


            result = convertView;
        }else{
            result = convertView;
        }

        nameSeason.setText(season.getName());
        edIdSeasonSelect.setTag(season);
        return convertView;
    }

}

