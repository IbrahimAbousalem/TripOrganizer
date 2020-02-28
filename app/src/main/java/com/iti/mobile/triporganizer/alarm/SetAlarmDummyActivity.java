package com.iti.mobile.triporganizer.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;

import com.iti.mobile.triporganizer.R;

public class SetAlarmDummyActivity extends AppCompatActivity {
    private AlarmService alarmService;
    private Button setAlarm,cancelAlarm,stopSound;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm_dummy);
        stopSound = findViewById(R.id.stop_sound);
        setAlarm = findViewById(R.id.set_alarm_btn);
        cancelAlarm = findViewById(R.id.cancel_alarm);


        enableReceiver();

        bindService(new Intent(SetAlarmDummyActivity.this, AlarmService.class),serviceConnection, Context.BIND_AUTO_CREATE);


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, AlarmBroadCastReceiver.class);
        //request code for pending intent must be unique on application level
        pendingIntent = PendingIntent.getBroadcast(this, 101, alarmIntent, 0);

        setAlarm.setOnClickListener((view)->{
            startAlarm();
        });
        cancelAlarm.setOnClickListener((view)->{
            cancelAlarm();
        });
        stopSound.setOnClickListener((view)->{
            if (alarmService !=null){
                alarmService.stopRingTone();
            }
        });

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
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlarmService.MyBinder myBinder = (AlarmService.MyBinder) iBinder;
            alarmService = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void startAlarm() {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 5*1000, pendingIntent);
    }

    private void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(), "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }
}