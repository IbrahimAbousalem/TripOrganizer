package com.iti.mobile.triporganizer.dagger.component;

import com.iti.mobile.triporganizer.dagger.Scope.ActivityScope;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.dagger.module.controller.ViewModelModule;
import com.iti.mobile.triporganizer.login.MainActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ControllerModule.class, ViewModelModule.class})
public interface ControllerComponent {
    void inject(MainActivity main);
}
