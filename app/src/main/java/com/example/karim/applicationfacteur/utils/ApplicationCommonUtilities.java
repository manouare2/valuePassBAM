package com.example.karim.applicationfacteur.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ApplicationCommonUtilities {

    public static boolean deviceConnectedToInternet(Context context) {
        if (context == null) return  false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) return false;

        return networkInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
}
