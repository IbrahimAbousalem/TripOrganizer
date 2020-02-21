package com.iti.mobile.triporganizer.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.iti.mobile.triporganizer.R;

import java.lang.ref.WeakReference;

public class GoogleConfiguration {

    private static  GoogleConfiguration mInstance;
    private WeakReference<Context> context;
    private GoogleSignInClient mGoogleSignInClient;

    private GoogleConfiguration(Context context) {
        this.context = new WeakReference<>(context);
    }

    public static GoogleConfiguration getInstance(Context context){
        if(mInstance == null){
            mInstance = new GoogleConfiguration(context);
        }
        return mInstance;
    }

    public void configureGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.get().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context.get(), gso);
    }

    public GoogleSignInClient getGoogleClient(){
        return mGoogleSignInClient;
    }
}
