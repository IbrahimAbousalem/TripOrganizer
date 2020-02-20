package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    public LiveData<User> signInWithEmailAndPasswordFunc(String email, String password) {
        return authenticationFirebase.signInWithEmailAndPasswordFunc(email,password);
    }

    @Override
    public LiveData<User> signInWithGoogleFunc(GoogleSignInAccount account) {
        return authenticationFirebase.signInWithGoogleFunc(account);
    }

    @Override
    public LiveData<User> signInWithFacebookFunc(AccessToken accessToken) {
        return authenticationFirebase.signInWithFacebookFunc(accessToken);
    }

    @Override
    public LiveData<User> getCurrentUser() {
        return authenticationFirebase.getCurrentUser();
    }

    @Override
    public void signoutFunc() {
        authenticationFirebase.signOutFunc();
    }
}
