package weather.widget.Classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import weather.widget.R;

public class NotificationCreater extends ContextWrapper {

    private final String CHANNEL_ID = "weather.widget.Classes";
    private final String CHANNEL_NAME = "NAME";
    private NotificationManager manager;
    private Drawable image;

    public NotificationCreater(Context base, Drawable image){
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
            this.image = image;
        }
    }

    private void createChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel);
        }

    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public Notification.Builder getChannelNotification(String title, String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                    .setContentText(message)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.appicon)
                    .setLargeIcon(Icon.createWithBitmap(((BitmapDrawable)image).getBitmap()))
                    .setAutoCancel(true);
        }
        return null;
    }

}
