package com.iti.mobile.triporganizer.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class FacebookUtils {
    public static String getReleaseKeyHash (Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
                return  Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        catch (NoSuchAlgorithmException e) {
            return  null;
        }
        return  null;
    }

}
