package com.iti.mobile.triporganizer.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    public static void showFragment(AppCompatActivity context, int containerId, Fragment fragment, String tag, boolean addToBackStack){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment, tag);
        if (addToBackStack){
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }
    public static void hideFragment(AppCompatActivity context, Fragment fragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }
}
