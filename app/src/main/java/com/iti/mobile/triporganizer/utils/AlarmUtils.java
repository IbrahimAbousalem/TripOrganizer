package com.iti.mobile.triporganizer.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.iti.mobile.triporganizer.alarm.AlarmBroadCastReceiver;
import com.iti.mobile.triporganizer.data.entities.Trip;

public class AlarmUtils {

    private static PendingIntent createPendingIntent(Context context, String tripName, String tripId, String destnationLatitude, String destinatinLongtiude) {

        Intent alarmIntent = new Intent(context, AlarmBroadCastReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + tripId));
        alarmIntent.setAction(tripId);
        alarmIntent.putExtra("tripName", tripName);
        alarmIntent.putExtra("tripId", tripId);
        alarmIntent.putExtra("destnationLatitude", destnationLatitude);
        alarmIntent.putExtra("destinatinLongtiude", destinatinLongtiude);

        //request code for pending intent must be unique on application level
        return PendingIntent.getBroadcast(context, 101, alarmIntent, 0);
    }


    public static void enableReceiver(Application application) {
        ComponentName receiver = new ComponentName(application, AlarmBroadCastReceiver.class);
        PackageManager pm = application.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    public static void disableReceiver(Application application) {
        ComponentName receiver = new ComponentName(application, AlarmBroadCastReceiver.class);
        PackageManager pm = application.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    public static void startAlarm(Context context, long triggerAtMillis, String tripName, String tripId, String destnationLatitude, String destinatinLongtiude) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, createPendingIntent(context, tripName, tripId, destnationLatitude, destinatinLongtiude));
        }
    }

    public static void cancelAlarm(Context context, String tripName, String tripId, String destnationLatitude, String destinatinLongtiude) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(createPendingIntent(context, tripName, tripId, destnationLatitude, destinatinLongtiude));
        }

    }
}
