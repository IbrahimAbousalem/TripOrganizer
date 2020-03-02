package com.iti.mobile.triporganizer.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.appHead.ChatHeadActivity;
import com.iti.mobile.triporganizer.appHead.ChatHeadService;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.utils.AlarmUtils;
import com.iti.mobile.triporganizer.utils.NotificationsUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.iti.mobile.triporganizer.alarm.AlarmService.foregroundId;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Cancel;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_End;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Snooze;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Start;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    private  AlarmManager alarmManager;
    private  PendingIntent pendingIntent;
    private TripOrganizerApp tripOrganizerApp;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        tripOrganizerApp = (TripOrganizerApp) context.getApplicationContext();

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

           // add all the alarms after rebooting so that the alarms survives the reboot process
        }else if (intent.getAction() != null && intent.getAction().equals(Action_Snooze)){
            //starts after 5 seconds
            //stop service


            String tripId, tripName, desLat, destLon;
            tripId = intent.getStringExtra("tripId");
            tripName = intent.getStringExtra("tripName");
            desLat = intent.getStringExtra("destnationLatitude");
            destLon = intent.getStringExtra("destinatinLongtiude");
//            Intent serviceIntent = new Intent(context, AlarmService.class);
//            serviceIntent.putExtra("tripName", tripName);
//            serviceIntent.putExtra("tripId", tripId);
//            serviceIntent.putExtra("destnationLatitude", desLat);
//            serviceIntent.putExtra("destinatinLongtiude", destLon);
            //AlarmUtils.cancelAlarm(context, tripName, tripId, desLat, destLon);

            AlarmUtils.startAlarmForSnooze(context, 240000, tripName, tripId, desLat, destLon);
            tripOrganizerApp.stopAlarmService();

        }else if (intent.getAction() != null && intent.getAction().equals(Action_Start)){
            //show the chat head


            if (Settings.canDrawOverlays(context)) {
                context.startService(new Intent(context, ChatHeadService.class).putExtra("tripId", intent.getStringExtra("tripId")).setFlags(FLAG_ACTIVITY_NEW_TASK));
            }
             if (tripOrganizerApp.getAlarmService()!=null){
                tripOrganizerApp.stopAlarmService();
            }
            //context.startActivity(new Intent(context, ChatHeadActivity.class).putExtra("tripId",intent.getStringExtra("tripId")).setFlags(FLAG_ACTIVITY_NEW_TASK));
            String tripId, desLat, destLon;
            tripId = intent.getStringExtra("tripId");
            desLat = intent.getStringExtra("destnationLatitude");
            destLon = intent.getStringExtra("destinatinLongtiude");
            Uri gmmIntentUri = Uri.parse("google.navigation:q="+desLat+","+destLon);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri).setFlags(FLAG_ACTIVITY_NEW_TASK);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
            tripOrganizerApp.stopSound();
            tripOrganizerApp.getAlarmService().stopForeground(true);
            tripOrganizerApp.getAlarmService().startForeground(foregroundId, NotificationsUtils.makeStatusNotificationForStartedTrip("started trip", getApplicationContext(),"started trip", tripId, desLat, destLon));

        }else if (intent.getAction() != null && intent.getAction().equals(Action_Cancel)){
            tripOrganizerApp.stopAlarmService();

        } else if (intent.getAction() != null && intent.getAction().equals(Action_End)){

            String tripId, desLat, destLon;
            tripId = intent.getStringExtra("tripId");
            desLat = intent.getStringExtra("destnationLatitude");
            destLon = intent.getStringExtra("destinatinLongtiude");
            //TODO do databasse changes
        }
        else {
            //bind service
            String tripId, tripName, desLat, destLon;
            tripId = intent.getStringExtra("tripId");
            tripName = intent.getStringExtra("tripName");
            desLat = intent.getStringExtra("destnationLatitude");
            destLon = intent.getStringExtra("destinatinLongtiude");
            Intent serviceIntent = new Intent(context, AlarmService.class);
            serviceIntent.putExtra("tripName", tripName);
            serviceIntent.putExtra("tripId", tripId);
            serviceIntent.putExtra("destnationLatitude", desLat);
            serviceIntent.putExtra("destinatinLongtiude", destLon);
            tripOrganizerApp.bindAlarmService();
            context.startService(serviceIntent);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 7*1000, pendingIntent);
        }else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 7*1000, pendingIntent);
        }
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
