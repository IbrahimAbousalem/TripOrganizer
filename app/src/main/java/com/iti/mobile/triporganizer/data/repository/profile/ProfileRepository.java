package com.iti.mobile.triporganizer.data.repository.profile;

import androidx.lifecycle.LiveData;

import com.iti.mobile.triporganizer.data.entities.User;

public interface ProfileRepository{
    LiveData<User> getCurrentUser();
    LiveData<Boolean> changeEmail(String email);
    LiveData<Boolean> changePassword(String password);
    LiveData<String> signoutFunc();
}
