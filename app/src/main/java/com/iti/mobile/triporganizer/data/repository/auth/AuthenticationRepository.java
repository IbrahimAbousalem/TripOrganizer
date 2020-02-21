package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.iti.mobile.triporganizer.data.entities.User;

public interface AuthenticationRepository {

    LiveData<User> signInWithEmailAndPasswordFunc(String email, String password);
    LiveData<User> signInWithGoogleFunc(GoogleSignInAccount account);
    LiveData<User> signInWithFacebookFunc(AccessToken accessToken);
    LiveData<User> getCurrentUser();
    LiveData<String> registerUser(User user, String password);
    void signoutFunc();

}
