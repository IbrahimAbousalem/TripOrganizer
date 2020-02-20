package com.iti.mobile.triporganizer.dagger.module.application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    @ApplicationScope
    @Provides
    FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @ApplicationScope
    @Provides
    FirebaseFirestore provideFirebaseFireStore(){
        return FirebaseFirestore.getInstance();
    }

}
