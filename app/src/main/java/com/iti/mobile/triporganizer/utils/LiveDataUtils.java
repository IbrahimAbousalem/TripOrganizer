package com.iti.mobile.triporganizer.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataUtils {
    public static <T> void observeOnce(LiveData<T> liveData, Observer<T> observer) {
        liveData.observeForever(o -> {
            liveData.removeObserver(observer);
            observer.onChanged(o);
        });
    }
}