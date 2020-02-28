package com.iti.mobile.triporganizer.app;

import android.app.Application;

import com.iti.mobile.triporganizer.dagger.component.ApplicationComponent;
import com.iti.mobile.triporganizer.dagger.component.DaggerApplicationComponent;
import com.iti.mobile.triporganizer.dagger.module.application.ApplicationModule;
import com.iti.mobile.triporganizer.dagger.module.application.RoomModule;

public class TripOrganizerApp extends Application {
    ApplicationComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();
        component.inject(this);
    }

    public ApplicationComponent getComponent(){
        return component;
    }

}
