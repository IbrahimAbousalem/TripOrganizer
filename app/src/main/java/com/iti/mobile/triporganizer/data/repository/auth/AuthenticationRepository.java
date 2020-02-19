package com.iti.mobile.triporganizer.data.repository.auth;

import androidx.lifecycle.LiveData;

public interface AuthenticationRepository {

    public LiveData<String> signInWithEmailAndPasswordFunc(String email, String password);
    public LiveData<String> getCurrentUserId();
}
