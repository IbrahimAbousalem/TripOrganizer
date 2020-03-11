package com.iti.mobile.triporganizer.dagger.component;

import com.iti.mobile.triporganizer.add_trip.AddTripFragment;
import com.iti.mobile.triporganizer.appHead.ChatHeadActivity;
import com.iti.mobile.triporganizer.appHead.ChatHeadService;
import com.iti.mobile.triporganizer.dagger.Scope.ActivityScope;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.dagger.module.controller.ViewModelModule;
import com.iti.mobile.triporganizer.details.DetailsFragment;
import com.iti.mobile.triporganizer.history.HistoryFragment;
import com.iti.mobile.triporganizer.login.LoginFragment;
import com.iti.mobile.triporganizer.base.MainActivity;
import com.iti.mobile.triporganizer.main.HomeFragment;
import com.iti.mobile.triporganizer.profile.ProfileFragment;
import com.iti.mobile.triporganizer.register.RegisterFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ControllerModule.class, ViewModelModule.class})
public interface ControllerComponent {
    void inject(MainActivity main);
    void inject(ChatHeadActivity chatHeadActivity);
    void inject(LoginFragment login);
    void inject(RegisterFragment register);
    void inject(HistoryFragment history);
    void inject(HomeFragment home);
    void inject(DetailsFragment details);
    void inject(AddTripFragment addTrip);
    void inject(ProfileFragment profileFragment);
    void inject(ChatHeadService chatHeadService);
}
