package com.iti.mobile.triporganizer.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.iti.mobile.triporganizer.alarm.AlarmService;
import com.iti.mobile.triporganizer.dagger.component.ApplicationComponent;
import com.iti.mobile.triporganizer.dagger.component.DaggerApplicationComponent;
import com.iti.mobile.triporganizer.dagger.module.application.ApplicationModule;
import com.iti.mobile.triporganizer.dagger.module.application.RoomModule;
import com.iti.mobile.triporganizer.utils.AlarmUtils;

public class TripOrganizerApp extends Application {
    private AlarmService alarmService;
    ApplicationComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        component.inject(this);
        AlarmUtils.enableReceiver(this);
    }

    public ApplicationComponent getComponent(){
        return component;
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
    public void bindAlarmService(){
        bindService(new Intent(this, AlarmService.class),serviceConnection, Context.BIND_AUTO_CREATE);

    }
    public void stopAlarmService(){
        //alarmService.stopForeground(true);
        alarmService.destroyService();
    }
    public void stopSound(){
        if (alarmService !=null){
            alarmService.stopRingTone();
        }
    }
    public AlarmService getAlarmService(){
        return alarmService;
    }

    @Override
    public void onTerminate() {
        alarmService.destroyService();
        super.onTerminate();
    }
}
