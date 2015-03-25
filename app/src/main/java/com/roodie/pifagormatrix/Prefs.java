package com.roodie.pifagormatrix;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Roodie on 22.03.2015.
 */
public class Prefs {

   private static String KEY_DARK_THEME = "dark_theme";
   private static String KEY_DATABASE_EXTRACTED = "database_extracted";

    private Prefs() {
        // Prevent instantiation
    }

    public static void setDarkTheme(Context context, boolean isDark) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(KEY_DARK_THEME, isDark).apply();
    }

    public static boolean isDarkTheme(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY_DARK_THEME, false);
    }

    public static boolean isDatabaseExtracted(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY_DATABASE_EXTRACTED, false);
    }

    public static void setDatabaseExtracted(Context context, boolean isExtracted) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(KEY_DATABASE_EXTRACTED, isExtracted).apply();
    }



}
