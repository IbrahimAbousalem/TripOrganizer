package com.iti.mobile.triporganizer.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    AuthenticationRepository authenticationRepositoryImp;

    @Inject
    public LoginViewModel(AuthenticationRepository repo){
        this.authenticationRepositoryImp = repo;
    }

    public LiveData<User> signInWithEmailAndPasswordVM(String email, String password){
        return authenticationRepositoryImp.signInWithEmailAndPasswordFunc(email,password);
    }

    public LiveData<User> signInWithGoogleVM(GoogleSignInAccount account){
        return authenticationRepositoryImp.signInWithGoogleFunc(account);
    }

    public LiveData<User> signInWithEFacebookVM(AccessToken accessToken){
        return authenticationRepositoryImp.signInWithFacebookFunc(accessToken);
    }

    public LiveData<User> getCurrentUser(){
        return authenticationRepositoryImp.getCurrentUser();
    }
    public void signOutVM(){
        authenticationRepositoryImp.signoutFunc();
    }

}
