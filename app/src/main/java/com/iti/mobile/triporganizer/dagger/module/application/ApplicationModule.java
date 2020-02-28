package com.iti.mobile.triporganizer.dagger.module.application;

import android.app.Application;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @ApplicationScope
    @Provides
    Application provideApplication(){
        return mApplication;
    }

}
