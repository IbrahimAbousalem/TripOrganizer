package com.iti.mobile.triporganizer.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class AuthenticationFirebase {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
   // private static AuthenticationFirebase INSTANCE;
    private static final String TAG = "AuthenticationFirebase";
    FirebaseFirestore db;

    @Inject
    public AuthenticationFirebase(FirebaseAuth auth, FirebaseFirestore db) {
        this.firebaseAuth = auth;
        this.db = db;
    }
//    private AuthenticationFirebase(){
//        firebaseAuth = FirebaseAuth.getInstance();
//    }
//    public static AuthenticationFirebase getInstance(){
//        if (INSTANCE==null){
//            INSTANCE=new AuthenticationFirebase();
//        }
//        return INSTANCE;
//    }

    public LiveData<String> signInWithEmailAndPasswordFunc(String email, String password) {
        MutableLiveData<String> currentUserId=new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.i(TAG, "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        currentUserId.postValue(user.getUid());

                    }else{
                        Log.i(TAG, "signInWithEmail:failure", task.getException());
                        currentUserId.postValue("");
                    }
                });
        return currentUserId;
    }

    public LiveData<String> signInWithGoogleFunc(GoogleSignInAccount account){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        MutableLiveData<String> currentUserId=new MutableLiveData<>();
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(TAG, "signInWithGoogle:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    currentUserId.postValue(user.getUid());

                }else{
                    Log.i(TAG, "signInWithGoogle:failure", task.getException());
                    currentUserId.postValue("");
                }
            }
        });
        return currentUserId;
    }

    public LiveData<String> getCurrentUserId(){
        MutableLiveData<String> currentUserId= new MutableLiveData<>();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
             currentUserId.postValue(firebaseUser.getUid());
        }else{
             currentUserId.postValue("");
        }
        return currentUserId;
    }

    public void signOutFunc(){
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            FirebaseAuth.getInstance().signOut();
        }
    }
}
