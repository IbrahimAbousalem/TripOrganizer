package com.iti.mobile.triporganizer.register;

import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    AuthenticationRepository authenticationRepositoryImp;

    @Inject
    public RegisterViewModel(AuthenticationRepository repo){
        this.authenticationRepositoryImp = repo;
    }

    public LiveData<String> registerUser(User user, String password){
        return authenticationRepositoryImp.registerUser(user, password);
    }
}
