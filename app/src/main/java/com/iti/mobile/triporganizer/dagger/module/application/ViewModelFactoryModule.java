package com.iti.mobile.triporganizer.dagger.module.application;

import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
@Module
public abstract class ViewModelFactoryModule {
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);
}
