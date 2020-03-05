package com.iti.mobile.triporganizer.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.ref.WeakReference;

public class Connection {

    private static Connection connection;
    private WeakReference<Context> context;

    private Connection(Context context){
        this.context = new WeakReference<>(context);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static Connection getInstance(Context context){
        if(connection == null){
            connection = new Connection(context);
        }
        return connection;
    }
}
