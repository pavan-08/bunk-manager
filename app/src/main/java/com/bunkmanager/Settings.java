package com.bunkmanager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.material_indigo_700));
        }
        final AlarmManager am = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
        Intent intent1 = new Intent(getBaseContext(), notify.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        String state = new String();
        String sHour=new String();
        String sMinute=new String();
        setContentView(R.layout.activity_settings);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput("notify")));
                String input;
                while ((input = inputReader.readLine()) != null) {
                    state=input;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("hour")));
            String input;
            while ((input = inputReader.readLine()) != null) {
                sHour=input;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("minute")));
            String input;
            while ((input = inputReader.readLine()) != null) {
                sMinute=input;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final TextView tv=(TextView)findViewById(R.id.tv);
        tv.setText("Reminder notifications will trigger daily at the time chosen by you"
        +" till kept on. It is simply a notification to tell you that the attendance has to be recorded for that day."+
        " So, preferably, set a time post your regular college/school hours.");
        final TextView text =(TextView)findViewById(R.id.textView10);
        final ImageView img=(ImageView)findViewById(R.id.imageView);
        final TextView text1=(TextView)findViewById(R.id.textView11);
        final SwitchCompat mswitch=(SwitchCompat)findViewById(R.id.switch1);
        Toolbar toolbar=(Toolbar)findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(state.equals("")||state.equals("off")){
            mswitch.setChecked(false);
            img.setImageResource(R.mipmap.notification_off);
            text1.setText("No reminder set");
            text.setText("Off");

        } else if(state.equals("on")){
            mswitch.setChecked(true);
            img.setImageResource(R.mipmap.notification_on);
            text1.setText("Reminder set, daily at "+String.format("%02d:%02d",Integer.parseInt(sHour),Integer.parseInt(sMinute)));
            text.setText("On");
        }

        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    final View layout = getLayoutInflater().inflate(R.layout.time_pick, null);
                    final TimePicker time = (TimePicker) layout.findViewById(R.id.timePicker);
                    time.setIs24HourView(true);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Settings.this);
                    dialog.setView(layout);
                    dialog.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSave("notify", "on", MODE_PRIVATE);
                            int hour = time.getCurrentHour();
                            int minute = time.getCurrentMinute();
                            mSave("hour", String.valueOf(hour), MODE_PRIVATE);
                            mSave("minute", String.valueOf(minute), MODE_PRIVATE);
                            long alarm=0;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.set(Calendar.SECOND, 00);
                            if(calendar.getTimeInMillis()<= System.currentTimeMillis()){
                                alarm=calendar.getTimeInMillis()+(AlarmManager.INTERVAL_DAY);
                            } else{
                                alarm=calendar.getTimeInMillis();
                            }
                            am.setRepeating(AlarmManager.RTC_WAKEUP, alarm, 24 * 60 * 60 * 1000, pendingIntent);
                            text1.setText("Reminder set, daily at " + String.format("%02d:%02d", hour, minute));
                            text.setText("On");
                            img.setImageResource(R.mipmap.notification_on);
                        }
                    });
                    dialog.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mswitch.setChecked(false);
                        }
                    });
                    dialog.show();

                } else {
                    am.cancel(pendingIntent);
                    mSave("notify", "off", MODE_PRIVATE);
                    text1.setText("No reminder" + " set");
                    text.setText("Off");
                    img.setImageResource(R.mipmap.notification_off);
                }

            }
        });

    }

    public  void mSave(String file, String data, int mode){
        FileOutputStream fos;
        try{
            fos=openFileOutput(file, mode);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}
