package com.iti.mobile.triporganizer.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.utils.NotificationsUtils;


public class AlarmService extends Service {
    MediaPlayer mp;
    Vibrator vibrator;
    public static final int foregroundId = 1;
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
        String tripId=null,tripName=null,desLat=null,destLon=null;
        tripId = intent.getStringExtra("tripId");
        tripName = intent.getStringExtra("tripName");
        desLat = intent.getStringExtra("destnationLatitude");
        destLon = intent.getStringExtra("destinatinLongtiude");

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
        startForeground(foregroundId, NotificationsUtils.makeStatusNotification(tripName, getApplicationContext(),tripName, tripId, desLat, destLon));
        return START_NOT_STICKY;
    }

    public void stopRingTone(){
        if (mp != null && mp.isPlaying()){
            mp.stop();
            mp.reset();
            mp.release();

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
        if (mp !=null){
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
        stopForeground(true);
        super.onDestroy();
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
}
