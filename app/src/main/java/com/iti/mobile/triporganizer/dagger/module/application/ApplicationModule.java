package com.iti.mobile.triporganizer.dagger.module.application;

import android.app.Application;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.firebase.AuthenticationFirebase;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;

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
    AuthenticationRepository provideAuthentication(AuthenticationFirebase firebase){
        return new AuthenticationRepositoryImp(firebase);
    }
}
