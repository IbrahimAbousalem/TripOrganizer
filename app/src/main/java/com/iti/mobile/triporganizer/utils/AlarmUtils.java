package com.iti.mobile.triporganizer.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;

import com.iti.mobile.triporganizer.alarm.AlarmBroadCastReceiver;
import com.iti.mobile.triporganizer.data.entities.Trip;

public class AlarmUtils {

    private static PendingIntent createPendingIntent(Context context, Trip trip) {

        Intent alarmIntent = new Intent(context, AlarmBroadCastReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + trip.getId()));
        alarmIntent.setAction(String.valueOf(trip.getId()));

        long tripId = trip.getId();
        Parcel parcel = Parcel.obtain();
        trip.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        alarmIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());

        //request code for pending intent must be unique on application level
        return PendingIntent.getBroadcast(context,  (int) tripId, alarmIntent, 0);
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


    public static void startAlarm(@NonNull Context context, long triggerAtMillis,@NonNull Trip trip) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, createPendingIntent(context, trip));
            }else {alarmManager.setExact(AlarmManager.RTC, triggerAtMillis, createPendingIntent(context, trip));}
        }
    }

    public static void startAlarmForSnooze(@NonNull Context context, long triggerAtMillis,@NonNull Trip trip) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long nextUpdateTimeMillis = System.currentTimeMillis() + 30 * DateUtils.MINUTE_IN_MILLIS;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextUpdateTimeMillis, createPendingIntent(context, trip));
            }else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdateTimeMillis, createPendingIntent(context, trip));
            }
        }
    }

    public static void cancelAlarm(@NonNull Context context,@NonNull Trip trip) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(createPendingIntent(context, trip));
        }

    }
}
