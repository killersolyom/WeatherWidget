package weather.widget.DataManager;

import android.app.Application;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class StorageManager {

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public StorageManager() {
        try {
            preference = getApplicationUsingReflection().getApplicationContext().getSharedPreferences("WeatherWidget", 0);
            editor = preference.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeData(String value, String key){
        editor.putString(key,value);
        editor.commit();
    }

    public String readData(String key) {
        return preference.getString(key, "");
    }

    public boolean isExistData(String key){
        return !preference.getString(key, "").equals("");
    }

    private static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
    }



}
