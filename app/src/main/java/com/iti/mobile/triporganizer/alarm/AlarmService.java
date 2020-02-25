package com.iti.mobile.triporganizer.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;

import com.iti.mobile.triporganizer.R;


public class AlarmService extends Service {
    MediaPlayer mp;
    Vibrator vibrator;
    public AlarmService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
       return  new MyBinder();
    }

    @Override
    public void onCreate() {
        if (mp == null){
            mp = new MediaPlayer();
        }
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mp.isPlaying()) {
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
        return START_STICKY;
    }

    public void stopRingTone(){
        if (mp != null){
            mp.stop();

        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
    class  MyBinder extends Binder {
        public AlarmService getService (){
            return AlarmService.this;
        }
    }
}
