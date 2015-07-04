package com.roodie.pifagormatrix;

import android.content.Context;
import android.view.View;

/**
 * Created by Roodie on 16.03.2015.
 */
    public class Utils {


    public enum ArrayDestination {
        GRID, DATABASE;
    }

    public enum BirthdayFormat {
        SHORT, LONG;
    }



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

   public static int getColor(Context context, int value, int normal) {
       if (value != 0) {
           if (value == normal)
               return context.getResources().getColor(R.color.grid_item_normal);
           else if (value < normal)
               return context.getResources().getColor(R.color.grid_item_less);
           else if (value > normal)
               return context.getResources().getColor(R.color.grid_item_larger);
       }
       return context.getResources().getColor(R.color.grid_item_default);

   }

    public static int getValueByString(String matrixValue) {
        int result;
        if (matrixValue.contains("-") || matrixValue.equals("")) {
            System.out.println("First");
            result =  -1;
        }
        else if (matrixValue.contains("(") || matrixValue.contains(")")) {
            System.out.println("Second");
                result =  Integer.parseInt(matrixValue.substring(0, matrixValue.indexOf("(")).trim());
        } else {
            System.out.println("None");
            result = Integer.parseInt(matrixValue);
        }
        System.out.println("Input string: " + matrixValue + " output: " + result);
        return result;
    }

    public static int simplifyNumberHalf(final int n) {
        final String string = Integer.toString(n);
        int n2 = 0;
        if (n < 0) {
            return n;
        }
        if (string.length() == 1) {
            return n;
        }
        for (int i = 0; i < string.length(); ++i) {
            n2 += Integer.valueOf(string.substring(i, i + 1));
        }
        String.valueOf(n2);
        return n2;
    }
}
