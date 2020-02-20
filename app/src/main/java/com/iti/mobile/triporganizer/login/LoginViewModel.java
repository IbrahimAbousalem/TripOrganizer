package com.iti.mobile.triporganizer.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    AuthenticationRepository authenticationRepositoryImp;

    @Inject
    public LoginViewModel(AuthenticationRepository repo){
        this.authenticationRepositoryImp = repo;
    }

    public LiveData<String> signInWithEmailAndPasswordVM(String email,String password){
        return authenticationRepositoryImp.signInWithEmailAndPasswordFunc(email,password);
    }

    public LiveData<String> signInWithGoogleVM(GoogleSignInAccount account){
        return authenticationRepositoryImp.signInWithGoogleFunc(account);
    }
    public LiveData<String> getCurrentUserId(){
        return authenticationRepositoryImp.getCurrentUserId();
    }
    public void signOutVM(){
        authenticationRepositoryImp.signoutFunc();
    }

}
