package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.firebase.AuthenticationFirebase;

import javax.inject.Inject;

public class AuthenticationRepositoryImp implements AuthenticationRepository{
    private AuthenticationFirebase authenticationFirebase;

    @Inject
    public AuthenticationRepositoryImp(AuthenticationFirebase fireAuth){
        this.authenticationFirebase = fireAuth;
    }

    @Override
    public LiveData<String> signInWithEmailAndPasswordFunc(String email, String password) {
        return authenticationFirebase.signInWithEmailAndPasswordFunc(email,password);
    }

    @Override
    public LiveData<String> signInWithFacebookFunc(AccessToken accessToken) {
        return authenticationFirebase.signInWithFacebookFunc(accessToken);
    }

    @Override
    public LiveData<User> getCurrentUser() {
        return authenticationFirebase.getCurrentUser();
    }

    @Override
    public LiveData<String> registerUser(User user, String password) {
        return authenticationFirebase.register(user, password);
    }

    @Override
    public void signoutFunc() {
        authenticationFirebase.signOutFunc();
    }

    @Override
    public LiveData<String> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        return authenticationFirebase.signInWithGoogle(googleAuthCredential);
    }
}
