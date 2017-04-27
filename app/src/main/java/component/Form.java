package component;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ryne.qr_device_scanner.ActivityInventory;
import com.example.ryne.qr_device_scanner.ActivityQRScanner;
import com.example.ryne.qr_device_scanner.R;

import java.util.ArrayList;

import data.HttpHandler;
import data.JSONDeviceParser;
import model.InventorySeason;

/**
 * Created by ryne on 27/04/2017.
 */

public class Form {
    public static RadioGroup rbgSeason;
    public static Button btDaSeason;
    public static Dialog openDialog;

    public static ArrayList<InventorySeason> arrSeason;
    public static InventorySeason inventorySeasonSelected;
    public static void initComponent(Context context){
        rbgSeason = (RadioGroup) openDialog.findViewById(R.id.rbgSeason);
        for(int i=0; i< arrSeason.size(); i++){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(arrSeason.get(i).getName());
            radioButton.setTag(arrSeason.get(i));
            rbgSeason.addView(radioButton);
        }
        openDialog.show();
        rbgSeason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int idex =  rbgSeason.getCheckedRadioButtonId();
                RadioButton rbSelected = (RadioButton) rbgSeason.findViewById(idex);
                inventorySeasonSelected = (InventorySeason) rbSelected.getTag();
            }
        });

        //ivSeasonSelected = inventorySeasonSelected;
    }
    public static void showDialogInventorySeasonSelect(Context context){
        openDialog = new Dialog(context);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialog_inventory_season);
        btDaSeason  = (Button) openDialog.findViewById(R.id.daButtonSeason);
        arrSeason = new ArrayList<>();
        InventorySeasonDataTasks inventorySeasonDataTasks = new InventorySeasonDataTasks(context);
        inventorySeasonDataTasks.execute("/inventory_seasons");
    }
    // view holder ở dây cho form

    // data tasks
    private static class InventorySeasonDataTasks extends AsyncTask<String, Void, String> {
        private Context context;
        public  InventorySeasonDataTasks(Context context){
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String dataInventorySeason = HttpHandler.makeServiceCall(params[0]);
            return dataInventorySeason;
        }
        @Override
        protected void onPostExecute(String dataInventorySeason) {
            arrSeason = JSONDeviceParser.getInventorySeason(dataInventorySeason);
            Log.d("arrsesson", arrSeason.toString());
            initComponent(context);
        }
    }
}
