package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    public LiveData<String> signInWithGoogleFunc(GoogleSignInAccount account) {
        return authenticationFirebase.signInWithGoogleFunc(account);
    }

    @Override
    public LiveData<String> getCurrentUserId() {
        return authenticationFirebase.getCurrentUserId();
    }

    @Override
    public void signoutFunc() {
        authenticationFirebase.signOutFunc();
    }
}
