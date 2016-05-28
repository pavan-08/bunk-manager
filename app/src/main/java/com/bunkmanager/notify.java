package com.bunkmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pavan on 10/06/2015.
 */
public class notify extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int requestID=(int) System.currentTimeMillis();
        Uri alarmsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent main = new Intent(context,MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =PendingIntent.getActivity(context,requestID,main,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setContentTitle("Bunk Manager");
        builder.setVibrate(new long[]{100, 400, 100, 400});
        builder.setSound(alarmsound);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setColor(context.getResources().getColor(R.color.material_indigo));
        builder.setContentIntent(pendingIntent);
        Date date =new Date();
        SimpleDateFormat dateFormat =new SimpleDateFormat("E");
        if(dateFormat.format(date).equals("Sun")){
            builder.setContentText("Its Sunday! Time to review attendance");
        } else {
            builder.setContentText("Tell me how your day was!");
        }
        builder.setSmallIcon(R.mipmap.ic_notification);
        Notification notification =builder.build();
        NotificationManager manager =(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(8,notification);
    }


}
