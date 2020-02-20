package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.iti.mobile.triporganizer.data.entities.User;

public interface AuthenticationRepository {

    public LiveData<User> signInWithEmailAndPasswordFunc(String email, String password);
    public LiveData<User> signInWithGoogleFunc(GoogleSignInAccount account);
    public LiveData<User> signInWithFacebookFunc(AccessToken accessToken);
    public LiveData<User> getCurrentUser();
    public void signoutFunc();

}
