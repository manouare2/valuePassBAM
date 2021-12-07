package com.example.karim.applicationfacteur.ui.main;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by karim on 24/06/2018.
 */

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    String login = "login";

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();

    }

    public void setUsername(String username) {
        editor.putString("login", username);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString("login", "");
    }

    public void setPassword(String password) {
        editor.putString("pwd", password);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString("pwd", "");
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
