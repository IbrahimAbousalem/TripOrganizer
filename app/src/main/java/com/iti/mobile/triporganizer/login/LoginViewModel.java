package com.iti.mobile.triporganizer.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    AuthenticationRepository authenticationRepositoryImp;

    @Inject
    public LoginViewModel(AuthenticationRepository repo){
        this.authenticationRepositoryImp = repo;
    }

    public LiveData<String> signInWithEmailAndPasswordVM(String email, String password){
        return authenticationRepositoryImp.signInWithEmailAndPasswordFunc(email,password);
    }

    public LiveData<String> signInWithEFacebookVM(AccessToken accessToken){
        return authenticationRepositoryImp.signInWithFacebookFunc(accessToken);
    }

    public LiveData<User> getCurrentUserVm(){
        return authenticationRepositoryImp.getCurrentUser();
    }
    public void signOutVM(){
         authenticationRepositoryImp.signoutFunc();
    }

    public LiveData<String> signInWithGoogle(AuthCredential googleAuthCredential) {
        return authenticationRepositoryImp.firebaseSignInWithGoogle(googleAuthCredential);
    }

}
