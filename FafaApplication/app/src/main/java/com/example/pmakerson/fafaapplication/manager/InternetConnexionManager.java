package com.example.pmakerson.fafaapplication.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by P Makerson on 23/03/2016.
 */
public class InternetConnexionManager {

    private Context context;

    public InternetConnexionManager(Context context) {
        this.context = context;

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
