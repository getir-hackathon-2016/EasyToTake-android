package com.easytotake.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {
    public static final String NAME = "com.kapgelco";
    public static final String SHOPPING = "SHOPPING";
    private static PreferencesManager preferencesManager = null;
    private SharedPreferences sharedPreferences;


    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    public int getShopping() {
        return sharedPreferences.getInt(SHOPPING, 0);
    }

    public void setShopping(int shoppingCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHOPPING, shoppingCount);
        editor.apply();
    }

    public void increment() {
        setShopping(getShopping() + 1);
    }

    public void decremenet() {
        setShopping(getShopping() - 1);
    }

}
