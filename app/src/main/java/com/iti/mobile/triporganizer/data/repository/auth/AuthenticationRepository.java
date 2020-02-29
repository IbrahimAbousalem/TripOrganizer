package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.iti.mobile.triporganizer.data.entities.User;

public interface AuthenticationRepository {

    LiveData<String> signInWithEmailAndPasswordFunc(String email, String password);
    LiveData<String> signInWithFacebookFunc(AccessToken accessToken);
    LiveData<User> getCurrentUser();
    LiveData<String> registerUser(User user, String password);
    void signoutFunc();
    LiveData<String> firebaseSignInWithGoogle(AuthCredential googleAuthCredential);
    LiveData<String> getCurrentUserId();
}
