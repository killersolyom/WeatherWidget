package weather.widget.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import weather.widget.DataManager.DataContainer;
import weather.widget.Database.DatabaseManager;
import weather.widget.Interfaces.IValuesChangeListenerForService;
import weather.widget.Widget.WeatherWidget;


public class ValueUpdaterService extends Service implements IValuesChangeListenerForService {

    @Override
    public void onCreate() {
        super.onCreate();
        DataContainer.getInstance().setListener(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        DatabaseManager.getInstance().getValues();
        super.onDestroy();
        Log.e("Appw","destroy service");
    }

    @Override
    public void onStart(Intent mIntent, int startId) {
        Log.e("Appw","Service onStart");
        DatabaseManager.getInstance().getValues();
    }


    @Override
    public void change(boolean status) {
        if(status){
            Intent intent = new Intent(getApplicationContext(), WeatherWidget.class);
            intent.setAction("UPDATE_WIDGET");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }
}