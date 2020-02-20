package com.iti.mobile.triporganizer.dagger.module.controller;

import com.iti.mobile.triporganizer.dagger.Scope.ViewModelKey;
import com.iti.mobile.triporganizer.login.LoginViewModel;
import com.iti.mobile.triporganizer.register.RegisterViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel(LoginViewModel searchModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    public abstract ViewModel bindRegisterViewModel(RegisterViewModel registerModel);
}
