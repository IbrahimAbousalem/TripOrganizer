package com.iti.mobile.triporganizer.data.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.data.room.TripOrganizerDatabase;
import com.iti.mobile.triporganizer.data.room.dao.UserDao;
import com.iti.mobile.triporganizer.data.shared_prefs.SharedPreferenceUtility;

import java.util.List;

import javax.inject.Inject;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.USERS_COLLECTION;

@ApplicationScope
public class AuthenticationFirebase {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static final String TAG = "AuthenticationFirebase";
    private FirebaseFirestore db;
    private UserDao userDao;
    private SharedPreferenceUtility sharedPref;

    @Inject
    public AuthenticationFirebase(UserDao userDao, FirebaseAuth auth, FirebaseFirestore db, SharedPreferenceUtility sharedPref) {
        this.firebaseAuth = auth;
        this.db = db;
        this.userDao = userDao;
        this.sharedPref = sharedPref;
    }

    public LiveData<String> signInWithEmailAndPasswordFunc(String email, String password) {
        MutableLiveData<String> currentUserLiveData=new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.i(TAG, "signInWithEmail:success");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //TODO: I removed User Photo
                User currentUser=new User(user.getDisplayName(),"",user.getEmail(),user.getUid(),user.getProviderId());
                sharedPref.saveUserId(currentUser.getId());
                currentUserLiveData.postValue(currentUser.getId());
            }else{
                Log.i(TAG, "signInWithEmail:failure"+task.getException().getMessage());
                currentUserLiveData.postValue("Error:" +task.getException().getMessage());
            }
        });
        return currentUserLiveData;
    }

    public LiveData<String> signInWithGoogleFunc(GoogleSignInAccount account){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        MutableLiveData<String> currentUserLiveData=new MutableLiveData<>();
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.i(TAG, "signInWithGoogle:success");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                User currentUser=null;
                for (UserInfo userInfo:user.getProviderData()){
                    currentUser=new User(userInfo.getDisplayName(),userInfo.getPhotoUrl().toString(),userInfo.getEmail(),userInfo.getUid(),userInfo.getProviderId());
                }
                saveToDatabase(currentUser);
                User finalCurrentUser = currentUser;
                sharedPref.saveUserId(currentUser.getId());
                TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
                    userDao.insertUser(finalCurrentUser);
                });
                currentUserLiveData.postValue(currentUser.getId());

            }else{
                Log.i(TAG, "signInWithGoogle:failure", task.getException());
                currentUserLiveData.postValue("Error:" +task.getException().getMessage());
            }
        });
        return currentUserLiveData;
    }

    public LiveData<String> signInWithFacebookFunc(AccessToken accessToken){
        MutableLiveData<String> currentUserLiveData=new MutableLiveData<>();
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        User currentUser=null;
                        for (UserInfo userInfo:user.getProviderData()){
                            currentUser=new User(userInfo.getDisplayName(),userInfo.getPhotoUrl().toString(),userInfo.getEmail(),userInfo.getUid(),userInfo.getProviderId());
                        }
                        saveToDatabase(currentUser);
                        sharedPref.saveUserId(currentUser.getId());
                        User finalCurrentUser = currentUser;
                        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
                            userDao.insertUser(finalCurrentUser);
                        });
                        currentUserLiveData.postValue(currentUser.getId());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        currentUserLiveData.postValue("Error:" +task.getException().getMessage());
                    }
                });
        return currentUserLiveData;
    }

    public String getCurrentUserId(){
        return sharedPref.getUserId();
    }

    public LiveData<User> getCurrentUser(){
        MutableLiveData<User> currentUser= new MutableLiveData<>();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            String providerId=firebaseUser.getProviderId();
            if(providerId.equals("facebook.com")||providerId.equals("google.com")){
                for(UserInfo userInfo:firebaseUser.getProviderData()){
                    currentUser.postValue(new User(userInfo.getDisplayName(),userInfo.getPhotoUrl().toString(),userInfo.getEmail(),userInfo.getUid(),userInfo.getProviderId()));
                }
            }else{

            }
            //TODO : i Commented the user image.
            currentUser.postValue(new User(firebaseUser.getDisplayName(),"",firebaseUser.getEmail(),firebaseUser.getUid(),firebaseUser.getProviderId()));
        }else{
            currentUser.postValue(null);
        }
        return currentUser;
    }

    public void signOutFunc(){
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseAuth.signOut();
        }
        sharedPref.clearPref();
    }

    public MutableLiveData<String> register(User user, String password) {
        MutableLiveData<String> registerUser = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if(currentUser!= null){
                        user.setId(currentUser.getUid());
                        user.setProvider_id(currentUser.getProviderId());
                        saveToDatabase(user);
                        sharedPref.saveUserId(user.getId());
                        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
                            userDao.insertUser(user);
                        });
                        registerUser.postValue(user.getId());
                    }
                }else{
                    registerUser.postValue("Error:" +task.getException().getMessage());
                }
            }
        });
        return registerUser;
    }

    public void saveToDatabase(User user){
        db.collection(USERS_COLLECTION).document(user.getId()).set(user);
        //TODO: add onSuccess and on Failure
    }

    public MutableLiveData<String> signInWithGoogle(AuthCredential googleAuthCredential) {
        MutableLiveData<String> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                User currentUser=null;
                for (UserInfo userInfo:user.getProviderData()){
                    currentUser=new User(userInfo.getDisplayName(),userInfo.getPhotoUrl().toString(),userInfo.getEmail(),userInfo.getUid(),userInfo.getProviderId());
                }
                User finalCurrentUser = currentUser;
                TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
                    userDao.insertUser(finalCurrentUser);
                });
                sharedPref.saveUserId(currentUser.getId());
                saveToDatabase(currentUser);
                authenticatedUserMutableLiveData.postValue(currentUser.getId());
            } else {
                Log.e(TAG,"Error:" +authTask.getException().getMessage());
                authenticatedUserMutableLiveData.postValue(authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLiveData;
    }


}