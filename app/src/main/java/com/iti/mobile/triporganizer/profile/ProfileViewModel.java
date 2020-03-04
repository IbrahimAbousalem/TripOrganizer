package com.iti.mobile.triporganizer.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.repository.profile.ProfileRepository;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {
    ProfileRepository profileRepositoryImp;

    @Inject
    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepositoryImp =  profileRepository;
    }

    public String getCurrentUserVm(){
        return profileRepositoryImp.getCurrentUserId();
    }
    public LiveData<User> getUserFromRoom(String userId){return profileRepositoryImp.getUserFromRoom(userId); }
    public LiveData<Boolean> changeEmail(String email) {return profileRepositoryImp.changeEmail(email);}
    public LiveData<Boolean> changePassword(String password) {return profileRepositoryImp.changePassword(password);}
    public LiveData<String> signOutVM(){ return profileRepositoryImp.signoutFunc(); }

}
