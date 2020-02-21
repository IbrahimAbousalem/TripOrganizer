package com.iti.mobile.triporganizer.app;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.iti.mobile.triporganizer.dagger.component.ApplicationComponent;
//import com.iti.mobile.triporganizer.dagger.component.DaggerApplicationComponent;
import com.iti.mobile.triporganizer.dagger.module.application.ApplicationModule;

public class TripOrganizerApp extends Application {
    ApplicationComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
//        component = DaggerApplicationComponent.builder()
//                .applicationModule(new ApplicationModule(this))
//                .build();
      //  component.inject(this);
    }

    public ApplicationComponent getComponent(){
        return component;
    }

}