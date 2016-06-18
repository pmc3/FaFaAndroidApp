package com.example.pmakerson.fafaapplication.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by P Makerson on 21/03/2016.
 */
public class PreferenceManager {

    SharedPreferences prefs;

    private final String PREF_USER = "PREF_USER";
    private final String PREF_USER_TOKEN_KEY = "USER_TOKEN_KEY";

    public PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
    }

    public String getUserToken() {
        return prefs.getString(PREF_USER_TOKEN_KEY, "");
    }

    public void setUserToken(String tokenValue) {
        prefs.edit().putString(PREF_USER_TOKEN_KEY, tokenValue).apply();
    }
}
