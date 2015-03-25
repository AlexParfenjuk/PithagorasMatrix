package com.roodie.pifagormatrix;

import android.content.Context;
import android.view.View;

/**
 * Created by Roodie on 16.03.2015.
 */
public class Utils {


    private Utils() {}

    public static boolean isOnlySymbols(final String s) {
        for (final char c : s.toCharArray()) {
            if (!Character.isLetter(c)) {
                final boolean b = false;
                if (c != '\'') {
                    return b;
                }
            }
        }
        return true;
    }


    public static int getTheme(Context context) {
        if (Prefs.isDarkTheme(context)) {
            return R.style.AppThemeDark;
        } else {
            return R.style.AppThemeLight;
        }
    }
}
