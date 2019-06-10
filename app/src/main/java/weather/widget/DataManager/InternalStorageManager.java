package weather.widget.DataManager;

import android.app.Application;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class InternalStorageManager {

    private SharedPreferences preference;
    SharedPreferences.Editor editor;

    public InternalStorageManager() {
        try {
            preference = getApplicationUsingReflection().getApplicationContext().getSharedPreferences("WeatherWidget", 0);
            editor = preference.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeData(String stationName){
        editor.putString("sName",stationName);
        editor.commit();
    }

    public String readData() {
        return preference.getString("sName", "");
    }

    public boolean isExist(){
        return !preference.getString("sName", "").equals("");
    }


    private static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
    }



}
