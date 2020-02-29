package com.iti.mobile.triporganizer.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;

public class SetAlarmDummyActivity extends AppCompatActivity {

    private Button setAlarm,cancelAlarm,stopSound;
    private  AlarmManager alarmManager;
    private  PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_dummy);
        stopSound = findViewById(R.id.stop_sound);
        setAlarm = findViewById(R.id.set_alarm_btn);
        cancelAlarm = findViewById(R.id.cancel_alarm);


        enableReceiver();
        Trip trip = new Trip();
        trip.setId(1234);


        createPendingIntent(String.valueOf(trip.getId()));

        setAlarm.setOnClickListener((view)->{
            startAlarm(String.valueOf(trip.getId()));
        });
        cancelAlarm.setOnClickListener((view)->{
            cancelAlarm(String.valueOf(trip.getId()));
        });
        stopSound.setOnClickListener((view)->{
            /*
            if (alarmService !=null){
                alarmService.stopRingTone();
            }
             */
        });

    }

    private PendingIntent createPendingIntent(String tripId) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, AlarmBroadCastReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + tripId));
        alarmIntent.setAction(tripId);

        //request code for pending intent must be unique on application level
        return PendingIntent.getBroadcast(this, 101, alarmIntent, 0);
    }


    private void enableReceiver() {
        ComponentName receiver = new ComponentName(getApplication(), AlarmBroadCastReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    private void disableReceiver() {
        ComponentName receiver = new ComponentName(getApplication(), AlarmBroadCastReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    private void startAlarm(String tripId) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 5*1000, createPendingIntent(tripId));
    }

    private void cancelAlarm(String tripId) {
        alarmManager.cancel(createPendingIntent(tripId));

    }
}
