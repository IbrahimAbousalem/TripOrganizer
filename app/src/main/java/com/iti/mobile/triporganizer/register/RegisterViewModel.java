package com.iti.mobile.triporganizer.register;

import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    AuthenticationRepository authenticationRepositoryImp;

    @Inject
    public RegisterViewModel(AuthenticationRepository repo){
        this.authenticationRepositoryImp = repo;
    }
}
