package com.iti.mobile.triporganizer.dagger.module.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.utils.Constants;

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

    @ApplicationScope
    @Provides
    SharedPreferences provideSharedPreference(){
        return mApplication.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
}
