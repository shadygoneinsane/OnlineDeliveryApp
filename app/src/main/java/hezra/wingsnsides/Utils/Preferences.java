package hezra.wingsnsides.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Vikesh on 9/3/2016.
 */
public class Preferences {

    public static void setLongPreference(Context context, final String key, final Long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void setStringPreference(Context context, final String key, final String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBooleanPreference(Context context, final String key, final boolean value) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getStringPreference(Context context, final String key) {

        String value = null;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            value = preferences.getString(key, Constants.EMPTY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static boolean getBooleanPreference(Context context, final String key) {
        boolean value = false;
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            value = preferences.getBoolean(key, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static long getLongPreference(Context context, final String key) {
        long value = 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        value = preferences.getLong(key, 0);
        return value;
    }

    public static void setIntPreference(Context context, final String key, final int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntPreference(Context context, final String key) {
        int value = 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        value = preferences.getInt(key, 0);
        return value;
    }
}
