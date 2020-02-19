package com.iti.mobile.triporganizer.data.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;

public class AuthViewModel extends ViewModel {
    AuthenticationRepositoryImp authenticationRepositoryImp=new AuthenticationRepositoryImp();

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
