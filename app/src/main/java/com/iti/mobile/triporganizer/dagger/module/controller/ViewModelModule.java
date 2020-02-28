package com.iti.mobile.triporganizer.dagger.module.controller;

import com.iti.mobile.triporganizer.add_trip.AddTripViewModel;
import com.iti.mobile.triporganizer.dagger.Scope.ViewModelKey;
import com.iti.mobile.triporganizer.details.DetailsViewModel;
import com.iti.mobile.triporganizer.history.HistoryViewModel;
import com.iti.mobile.triporganizer.login.LoginViewModel;
import com.iti.mobile.triporganizer.main.TripsViewModel;
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

    @Binds
    @IntoMap
    @ViewModelKey(TripsViewModel.class)
    public abstract ViewModel bindTripsViewModel(TripsViewModel tripsModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel.class)
    public abstract ViewModel bindDetailsViewModel(DetailsViewModel detailsModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    public abstract ViewModel bindHistoryViewModel(HistoryViewModel historyModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddTripViewModel.class)
    public abstract ViewModel bindAddTripViewModel(AddTripViewModel addTripModel);
}
