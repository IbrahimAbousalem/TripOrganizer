package com.iti.mobile.triporganizer.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.appHead.ChatHeadActivity;
import com.iti.mobile.triporganizer.appHead.ChatHeadService;
import com.iti.mobile.triporganizer.dagger.module.controller.BroadCastReceiverModule;
import com.iti.mobile.triporganizer.dagger.module.controller.ChatHeadServiceControllerModule;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.room.TripsRoom;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;
import com.iti.mobile.triporganizer.utils.AlarmUtils;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.NotificationsUtils;

import javax.inject.Inject;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.iti.mobile.triporganizer.alarm.AlarmService.foregroundId;
import static com.iti.mobile.triporganizer.utils.Constants.NO_DATA;
import static com.iti.mobile.triporganizer.utils.Constants.TRIP_INTENT;
import static com.iti.mobile.triporganizer.utils.Constants.USER_ID;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Cancel;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_End;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Snooze;
import static com.iti.mobile.triporganizer.utils.NotificationsUtils.Action_Start;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    private  AlarmManager alarmManager;
    private  PendingIntent pendingIntent;
    private TripOrganizerApp tripOrganizerApp;
    @Inject
    TripsRoom tripsRoom;
    @Inject
    SharedPreferences sharedPref;
    private Trip trip;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        tripOrganizerApp = (TripOrganizerApp) context.getApplicationContext();
        tripOrganizerApp.getComponent().newBroadCastReceiverComponent(new BroadCastReceiverModule(this)).inject(this);

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

        }else if (intent.getAction() != null && intent.getAction().equals(Action_Snooze)){
            if (intent.hasExtra(Constants.TRIP_INTENT)) {
                parcelTripObject(intent);
            }

            AlarmUtils.startAlarmForSnooze(context, 30*1000, trip);
            //tripOrganizerApp.stopAlarmService();
            tripOrganizerApp.stopSound();
            tripOrganizerApp.getAlarmService().stopForeground(true);

        }else if (intent.getAction() != null && intent.getAction().equals(Action_Start)){
            //show the chat head

            if (intent.hasExtra(Constants.TRIP_INTENT)) {
                parcelTripObject(intent);
            }

            if (Settings.canDrawOverlays(context)) {
                Intent chatHeadIntent = new Intent(context, ChatHeadService.class);
                Parcel parcel = Parcel.obtain();
                trip.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                chatHeadIntent.putExtra(Constants.TRIP_INTENT, parcel.marshall());
                context.startService(chatHeadIntent);
            }
             if (tripOrganizerApp.getAlarmService()!=null){
                tripOrganizerApp.stopAlarmService();
            }
            //context.startActivity(new Intent(context, ChatHeadActivity.class).putExtra("tripId",intent.getStringExtra("tripId")).setFlags(FLAG_ACTIVITY_NEW_TASK));

            Uri gmmIntentUri = Uri.parse("google.navigation:q="+trip.getLocationData().getStartTripEndPointLat()+","+trip.getLocationData().getStartTripEndPointLng());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri).setFlags(FLAG_ACTIVITY_NEW_TASK);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
            tripOrganizerApp.stopSound();
            tripOrganizerApp.getAlarmService().stopForeground(true);
            tripOrganizerApp.getAlarmService().startForeground(foregroundId, NotificationsUtils.makeStatusNotificationForStartedTrip("started trip", getApplicationContext(),trip.getTripName(), String.valueOf(trip.getId()), String.valueOf(trip.getLocationData().getStartTripStartPointLat()), String.valueOf(trip.getLocationData().getStartTripStartPointLng())));

        }else if (intent.getAction() != null && intent.getAction().equals(Action_Cancel)){
            tripOrganizerApp.stopAlarmService();
            if (intent.hasExtra(Constants.TRIP_INTENT)) {
                parcelTripObject(intent);
            }
            tripsRoom.updateTripStatus(trip.getId(), trip.getUserId(), Constants.CANCELED);
        } else if (intent.getAction() != null && intent.getAction().equals(Action_End)){

            if (intent.hasExtra(Constants.TRIP_INTENT)) {
                parcelTripObject(intent);
            }
            tripsRoom.updateTripStatus(trip.getId(), trip.getUserId(), Constants.FINISHED);

            tripOrganizerApp.getAlarmService().stopForeground(true);
            if (tripOrganizerApp.getAlarmService()!=null){
                tripOrganizerApp.stopAlarmService();
            }
            //TODO handle roundTrip logic
        }
        else {
            //bind service
            if (intent.hasExtra(Constants.TRIP_INTENT)) {
                parcelTripObject(intent);
            }
            Intent serviceIntent = new Intent(context, AlarmService.class);
            Parcel parcel = Parcel.obtain();
            trip.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            serviceIntent.putExtra(TRIP_INTENT, parcel.marshall());
            tripOrganizerApp.bindAlarmService();
            context.startService(serviceIntent);
            //context.startActivity(new Intent(context, SetAlarmDummyActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void parcelTripObject(Intent intent) {
        byte[] byteArrayExtra = intent.getByteArrayExtra(Constants.TRIP_INTENT);
        Parcel parcel = Parcel.obtain();
        if (byteArrayExtra != null) {
            parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
        }
        parcel.setDataPosition(0);
        trip = Trip.CREATOR.createFromParcel(parcel);
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
