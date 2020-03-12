package com.iti.mobile.triporganizer.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Vibrator;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.NotificationsUtils;


public class AlarmService extends Service {
    MediaPlayer mp;
    Vibrator vibrator;
    public static final int foregroundId = 1;
    private Trip trip;
    public AlarmService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
       return  new MyBinder();
    }

    @Override
    public void onCreate() {

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra(Constants.TRIP_INTENT)) {
            parcelTripObject(intent);
        }

         if (mp == null){
        // mp = new MediaPlayer();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.belevier);
        }
        mp.setVolume(100, 100);
        mp.start();
        mp.setOnCompletionListener((mp) -> {
            mp.release();
        });


        if (vibrator !=null){
            vibrator.vibrate(400);
        }
        //WHY
        startForeground(foregroundId, NotificationsUtils.makeStatusNotification(trip.getTripName(), getApplicationContext(),trip.getTripName(), String.valueOf(trip.getId()), String.valueOf(trip.getLocationData().getStartTripEndPointLat()), String.valueOf(trip.getLocationData().getStartTripEndPointLng())));
        return START_NOT_STICKY;
    }

    public void stopRingTone(){
        if (mp != null && mp.isPlaying()){
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;

        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
    public void destroyService(){
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp !=null){
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
        stopForeground(true);

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
    public class  MyBinder extends Binder {
        public AlarmService getService (){
            return AlarmService.this;
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
}
