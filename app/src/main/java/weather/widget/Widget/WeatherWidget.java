package weather.widget.Widget;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RemoteViews;
import java.util.Calendar;
import java.util.Objects;
import weather.widget.Classes.NotificationCreater;
import weather.widget.DataManager.DataContainer;
import weather.widget.Main.MainActivity;
import weather.widget.R;


public class WeatherWidget extends AppWidgetProvider{

    static String CLICK_ACTION = "CLICKED";
    private static Context mContext;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        mContext = context;
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        String values = "Hőmérsékelt:  " +DataContainer.getInstance().getTemperature() +" C\n";
        values += "Eső:  " + DataContainer.getInstance().getRainlevel()+" L/m²\n";
        values += "Páratartalom:  " + DataContainer.getInstance().getHumidity()+" %";
        views.setTextViewText(R.id.valueView, values);
        views.setImageViewResource(R.id.widgetLogo,R.drawable.appicon);

        views.setOnClickPendingIntent(R.id.layout_wrapper,pendingIntent);
        views.setOnClickPendingIntent(R.id.valueView,pendingIntent);
        views.setOnClickPendingIntent(R.id.widgetLogo,pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        update(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(Objects.equals(intent.getAction(), CLICK_ACTION)){
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
        }else if (Objects.equals(intent.getAction(), "UPDATE_WIDGET")) {
            //Log.e("Appw","Widget update\n");
            update(context);
        }

    }

    private static void update(Context context){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.weather_widget);
        if(Double.parseDouble(DataContainer.getInstance().getWindSpeed()) > 85){
            remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.tornado);
           try {
               createTornadoNotification("Ön veszélyben van!", "A környéken tornádó van! Meneküljön!", ContextCompat.getDrawable(getApplicationUsingReflection().getApplicationContext(),R.drawable.tornado));
           }catch (Exception ignored){}
        }else if(Double.parseDouble(DataContainer.getInstance().getUvLevel()) > 8){
            remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.uv);
            try {
                createTornadoNotification("Legyen óvatos!", "Az Uv Index veszélyesen magas!", ContextCompat.getDrawable(getApplicationUsingReflection().getApplicationContext(),R.drawable.dangerousuv));
            }catch (Exception ignored){}
        }else if(isNight()){
            if(Double.parseDouble(DataContainer.getInstance().getTemperature()) <= 0){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.snowynight);
            }else if(Double.parseDouble(DataContainer.getInstance().getTemperature()) <= 0 && Double.parseDouble(DataContainer.getInstance().getWindSpeed()) > 5){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.windysnow);
            }else if(Double.parseDouble(DataContainer.getInstance().getRainlevel()) > 0){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.rainynight);
            }else if(Double.parseDouble(DataContainer.getInstance().getRainlevel()) > 0 && Double.parseDouble(DataContainer.getInstance().getTemperature()) <= 15){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.rainycold);
            }else if(Double.parseDouble(DataContainer.getInstance().getWindSpeed()) > 5){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.windynight);
            }else if(Double.parseDouble(DataContainer.getInstance().getWindSpeed()) > 5 && Double.parseDouble(DataContainer.getInstance().getRainlevel()) > 0){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.windyrainnight);
            }else if(Double.parseDouble(DataContainer.getInstance().getWindSpeed()) < 5 && Double.parseDouble(DataContainer.getInstance().getRainlevel()) < 0.3 ){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.night);
            }else {
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.night);
            }
        }else{
            if( Double.parseDouble(DataContainer.getInstance().getTemperature()) >= 18 && Double.parseDouble(DataContainer.getInstance().getRainlevel()) < 2) {
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.sunny );
            }else if(Double.parseDouble(DataContainer.getInstance().getTemperature()) <= 0){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.snowyday);
            }else if(Double.parseDouble(DataContainer.getInstance().getTemperature()) <= 0 && Double.parseDouble(DataContainer.getInstance().getWindSpeed()) >= 5){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.windysnow);
            }else if(Double.parseDouble(DataContainer.getInstance().getTemperature()) > 0 && Double.parseDouble(DataContainer.getInstance().getWindSpeed()) < 5 && Double.parseDouble(DataContainer.getInstance().getRainlevel()) <= 0.5){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.cloudy);
            }else if(Double.parseDouble(DataContainer.getInstance().getRainlevel()) > 0){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.rainyday);
            }else if(Double.parseDouble(DataContainer.getInstance().getRainlevel()) > 0 && Double.parseDouble(DataContainer.getInstance().getTemperature()) <= 15){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.rainycold);
            }else if(Double.parseDouble(DataContainer.getInstance().getWindSpeed()) > 5){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.windy);
            }else if(Double.parseDouble(DataContainer.getInstance().getWindSpeed()) > 5 && Double.parseDouble(DataContainer.getInstance().getRainlevel()) > 0){
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.windyrain);
            }else {
                remoteViews.setImageViewResource(R.id.widgetLogo, R.drawable.appicon);
            }
        }
        String values = "Hőmérséklet:  " +DataContainer.getInstance().getTemperature() +" °C\n";
        values += "Eső:  " + DataContainer.getInstance().getRainlevel()+" L/m²\n";
        values += "Páratartalom:  " + DataContainer.getInstance().getHumidity()+" %";
        remoteViews.setTextViewText(R.id.valueView, values);
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, WeatherWidget.class), remoteViews);
    }

    private static void createTornadoNotification(String title,String message, Drawable image){
        try {
            NotificationCreater notificationCreater = new NotificationCreater(getApplicationUsingReflection().getBaseContext(),image);
            Notification.Builder notify = notificationCreater.getChannelNotification(title,message);
            notificationCreater.getManager().notify(0,notify.build());
        }catch (Exception ignored){
        }
    }

    private static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
    }

    private static boolean isNight(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if( (hour >= 21) || (hour >=0 && hour <= 6)){
            return true;
        }
        return false;
    }


}

