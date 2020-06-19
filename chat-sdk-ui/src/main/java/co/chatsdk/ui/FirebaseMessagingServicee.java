package co.chatsdk.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import co.chatsdk.ui.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingServicee extends FirebaseMessagingService {
    private static final String ID="Some ID";
    private  static final String NAME="Ramf";
    private NotificationManager notificationManager;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "getInstanceId failed", task.getException());
                                return;
                            }
                            else{
                                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
                                String token = task.getResult().getToken();
                                Token mToken =new Token(token);
                                ref.child(user.getUid()).setValue(mToken);
                            }





                        }
                    });
        }

    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPreferences sp= getSharedPreferences("RAMF", Context.MODE_PRIVATE);
        String savedCurrentUser=sp.getString("Current_USERID","None");


        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null && sent.equals(fuser.getUid())){

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    send0andAbove(remoteMessage);
                }
                else{
                    sendNormal(remoteMessage);
                }

        }



    }

    private void sendNormal(RemoteMessage remoteMessage) {
        String user= remoteMessage.getData().get("user");
        String icon= remoteMessage.getData().get("icon");
        String title= remoteMessage.getData().get("title");
        String body= remoteMessage.getData().get("body");
        RemoteMessage.Notification notification= remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent (this,HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent= PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        assert icon != null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int j=0;
        if(i>0){
            j=i;
        }
        notificationManager.notify(j,builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void send0andAbove(RemoteMessage remoteMessage) {
        NotificationChannel notificationChannel=new NotificationChannel(ID,NAME,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);


        String user= remoteMessage.getData().get("user");
        String icon= remoteMessage.getData().get("icon");
        String title= remoteMessage.getData().get("title");
        String body= remoteMessage.getData().get("body");
        RemoteMessage.Notification notification= remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent (this,HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent= PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        assert icon != null;
        Notification.Builder builder= new Notification.Builder(getApplicationContext(),ID)
                .setContentIntent(pIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defSoundUri)
                .setChannelId(ID)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));
        NotificationManager  notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);


        int j=0;
        if(i>0){
            j=i;
        }
        notificationManager.notify(j,builder.build());


    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void createchannel() {
//        NotificationChannel notificationChannel=new NotificationChannel(ID,NAME,NotificationManager.IMPORTANCE_DEFAULT);
//        notificationChannel.enableLights(true);
//        notificationChannel.enableVibration(true);
//        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//    }
//    public  NotificationManager getManager(){
//        if (notificationManager==null){
//            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        return  notificationManager;
//    }
}
