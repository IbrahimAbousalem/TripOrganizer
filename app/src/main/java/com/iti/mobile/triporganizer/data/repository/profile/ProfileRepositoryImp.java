package com.iti.mobile.triporganizer.data.repository.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.firebase.AuthenticationFirebase;

import javax.inject.Inject;

public class ProfileRepositoryImp implements ProfileRepository{
    private AuthenticationFirebase authenticationFirebase;

    @Inject
    public ProfileRepositoryImp(AuthenticationFirebase authenticationFirebase) {
        this.authenticationFirebase = authenticationFirebase;
    }


    @Override
    public LiveData<User> getCurrentUser() {
        return authenticationFirebase.getCurrentUser();
    }

    @Override
    public LiveData<Boolean> changeEmail(String email) {
        MutableLiveData<Boolean> isUpdated=new MutableLiveData<>();
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    isUpdated.postValue(true);
                }else{
                    isUpdated.postValue(false);
                }
            }
        });
        return isUpdated;
    }

    @Override
    public LiveData<Boolean> changePassword(String password) {
        MutableLiveData<Boolean> isUpdated=new MutableLiveData<>();
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    isUpdated.postValue(true);
                }else{
                    isUpdated.postValue(false);
                }
            }
        });
        return isUpdated;
    }

    @Override
    public LiveData<String> signoutFunc() {
        return authenticationFirebase.signOutFunc();
    }
}
