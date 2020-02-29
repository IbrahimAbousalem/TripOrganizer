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

public class AlarmUtils {

    private static PendingIntent createPendingIntent(Context context, String tripId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmBroadCastReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + tripId));
        alarmIntent.setAction(tripId);

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


    public static void startAlarm(Context context, String tripId, long triggerAtMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, createPendingIntent(context, tripId));
        }
    }

    public static void cancelAlarm(Context context, String tripId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(createPendingIntent(context, tripId));
        }

    }
}
