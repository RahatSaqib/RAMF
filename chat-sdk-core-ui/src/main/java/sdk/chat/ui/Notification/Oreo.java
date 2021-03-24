package sdk.chat.ui.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Oreo extends ContextWrapper {
    private static final String ID="Some ID";
    private  static final String NAME="Ramf";
    private NotificationManager notificationManager;
    public Oreo(Context base){
        super(base);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createchannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createchannel() {
        NotificationChannel notificationChannel=new NotificationChannel(ID,NAME,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

    }
    public  NotificationManager getManager(){
        if (notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  notificationManager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotifications(String title, String body, PendingIntent pInetent, Uri soundUri, String icon){
        return  new Notification.Builder(getApplicationContext(),ID)
                .setContentIntent(pInetent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setChannelId(ID)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));
    }
}
