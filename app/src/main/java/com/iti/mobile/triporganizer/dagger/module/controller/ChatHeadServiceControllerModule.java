package com.iti.mobile.triporganizer.dagger.module.controller;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.iti.mobile.triporganizer.appHead.ChatHeadService;
import com.iti.mobile.triporganizer.dagger.Scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatHeadServiceControllerModule {
    ChatHeadService mService;
    public ChatHeadServiceControllerModule(ChatHeadService mService) {
        this.mService = mService;
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return mService;
    }

    @ActivityScope
    @Provides
    ChatHeadService provideActivity(){
        return mService;
    }
}
