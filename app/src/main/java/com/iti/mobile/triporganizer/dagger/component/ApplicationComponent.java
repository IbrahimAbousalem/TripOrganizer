package com.iti.mobile.triporganizer.dagger.component;

import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.dagger.module.application.ApplicationModule;
import com.iti.mobile.triporganizer.dagger.module.application.FirebaseModule;
import com.iti.mobile.triporganizer.dagger.module.application.ViewModelFactoryModule;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {ApplicationModule.class, FirebaseModule.class, ViewModelFactoryModule.class})
public interface ApplicationComponent {
    ControllerComponent newActivityComponent(ControllerModule module);
    void inject(TripOrganizerApp application);
}
