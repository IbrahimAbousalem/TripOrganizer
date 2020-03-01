package com.iti.mobile.triporganizer.data.shared_prefs;

import android.content.SharedPreferences;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.utils.Constants;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@ApplicationScope
public class SharedPreferenceUtility {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor  editor;

    @Inject
    public SharedPreferenceUtility(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }

    public void saveUserId(String userId){
        editor = sharedPref.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public String getUserId(){
        return sharedPref.getString(Constants.USER_ID,Constants.NO_DATA);
    }

    public void clearPref(){
        editor = sharedPref.edit();
        editor.clear().apply();
    }
}

