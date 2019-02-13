package com.hyper.design.medicalcard.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Hyper Design Hussien on 1/22/2018.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_LANGUAGE_IN = "isLanguageIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean setDefaultLogin(Context context){
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        // commit changes
        editor.commit();
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLanguage(String isLanguageIn){
        editor.putString(KEY_IS_LANGUAGE_IN, isLanguageIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public String isLanguageIn(){
        return pref.getString(KEY_IS_LANGUAGE_IN , "en");
    }

}
