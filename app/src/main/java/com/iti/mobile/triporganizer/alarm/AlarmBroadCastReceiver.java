package com.iti.mobile.triporganizer.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import com.iti.mobile.triporganizer.app.TripOrganizerApp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Cancel;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Snooze;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Start;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    private  AlarmManager alarmManager;
    private  PendingIntent pendingIntent;
    TripOrganizerApp tripOrganizerApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        tripOrganizerApp = (TripOrganizerApp) context.getApplicationContext();
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

           // add all the alarms after rebooting so that the alarms survives the reboot process
        }else if (intent.getAction() != null && intent.getAction().equals(Action_Snooze)){
            //starts after 5 seconds
            //stop service

            tripOrganizerApp.stopAlarmService();

            startAlarm(context,"1234");
        }else if (intent.getAction() != null && intent.getAction().equals(Action_Start)){
            //show the chat head
        }else if (intent.getAction() != null && intent.getAction().equals(Action_Cancel)){
            tripOrganizerApp.stopAlarmService();
        }
        else {
            //bind service
            tripOrganizerApp.bindAlarmService();
            context.startService(new Intent(context,AlarmService.class));
            //context.startActivity(new Intent(context, SetAlarmDummyActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK));
        }


    }



    private void startAlarm(Context context, String tripId) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmBroadCastReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + tripId));
        alarmIntent.setAction(tripId);
        //request code for pending intent must be unique on application level
        pendingIntent = PendingIntent.getBroadcast(context, 101, alarmIntent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 7*1000, pendingIntent);
    }

    private void cancelAlarm(Context context, String tripId) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmBroadCastReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + tripId));
        alarmIntent.setAction(tripId);
        //request code for pending intent must be unique on application level
        pendingIntent = PendingIntent.getBroadcast(context, 101, alarmIntent, 0);

        alarmManager.cancel(pendingIntent);

    }




}
