package com.iti.mobile.triporganizer.dagger.module.controller;

import android.content.Context;

import com.iti.mobile.triporganizer.alarm.AlarmBroadCastReceiver;
import com.iti.mobile.triporganizer.dagger.Scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class BroadCastReceiverModule {
    AlarmBroadCastReceiver mReceiver;

    public BroadCastReceiverModule(AlarmBroadCastReceiver mReceiver) {
        this.mReceiver = mReceiver;
    }

    @ActivityScope
    @Provides
    AlarmBroadCastReceiver provideReceiver(){
        return mReceiver;
    }
}
