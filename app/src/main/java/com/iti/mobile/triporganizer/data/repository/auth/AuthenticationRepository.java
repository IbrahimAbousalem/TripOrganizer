package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface AuthenticationRepository {

    public LiveData<String> signInWithEmailAndPasswordFunc(String email, String password);
    public LiveData<String> signInWithGoogleFunc(GoogleSignInAccount account);
    public LiveData<String> getCurrentUserId();
    public void signoutFunc();

}
