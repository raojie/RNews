package com.raoj.news.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.raoj.news.app.App;

/**
 * class: Util
 * describe:
 * author: raoj
 * date: 2018-09-07-16:34
 */
public class Util {

    private static String username = null;

    public static String getUsername() {
        if (username != null)
            return username;
        SharedPreferences sharedPreferences= App.getContext().getSharedPreferences(Constant.PREFS_USERNAME_SAVE,
                Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Constant.PREFS_USERNAME_KEY, null);
        return username;
    }

    public static void setUserName(final String name) {
        username = name;
        // Save the text in SharedPreference
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(Constant.PREFS_USERNAME_SAVE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.PREFS_USERNAME_KEY, name);
        editor.apply();
    }
}
