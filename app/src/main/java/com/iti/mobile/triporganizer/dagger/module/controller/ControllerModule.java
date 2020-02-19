package com.iti.mobile.triporganizer.dagger.module.controller;

import com.iti.mobile.triporganizer.dagger.Scope.ActivityScope;
import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import dagger.Module;
import dagger.Provides;

@Module
public class ControllerModule {
    private FragmentActivity mActivity;

    public ControllerModule(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    @ActivityScope
    @Provides
    Context provideContext(){
        return mActivity;
    }

    @ActivityScope
    @Provides
    Activity provideActivity(){
        return mActivity;
    }

    @ActivityScope
    @Provides
    FragmentManager provideFragmentManager(){
        return mActivity.getSupportFragmentManager();
    }
}
