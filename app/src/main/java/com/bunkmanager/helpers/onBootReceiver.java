package com.bunkmanager.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bunkmanager.helpers.notify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by Pavan on 18/06/2015.
 */
public class onBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am= (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent intent1=new Intent(context,notify.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        String status=ReadLine("notify", context);
        String hour=ReadLine("hour",context);
        String minute=ReadLine("minute",context);
        if (status.equals("on") && !hour.equals("") && !minute.equals("")) {
            int mHour = Integer.parseInt(hour);
            int mMinute = Integer.parseInt(minute);
            long alarm = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, mHour);
            calendar.set(Calendar.MINUTE, mMinute);
            calendar.set(Calendar.SECOND, 00);
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY);
            } else {
                alarm = calendar.getTimeInMillis();
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, alarm, 24 * 60 * 60 * 1000, pendingIntent);

        }
    }
    public String ReadLine(String filename,Context context)
    {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    context.openFileInput(filename)));
            String inputString;

            while ((inputString = inputReader.readLine()) != null)
            {
                stringBuffer.append(inputString );
            }
        }catch (IOException e) {
            e.printStackTrace();}
        return stringBuffer.toString();
    }
}
