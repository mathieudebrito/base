package com.github.mathieudebrito.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefs {

    public static final String PREFS_CREATED = "preferences_created";
    public static final String PREFS_VERSION = "preferences_version";
    public static String app = "geovelo";
    public static boolean resetAtLaunch = false;
    public static boolean resetAtVersion = false;

    public static boolean shouldReset(Context context, String versionName) {
        boolean shouldReset = false;

        // App need an startTracking
        if (!getBoolean(context, PREFS_CREATED)) {
            return true;
        }

        // It's a new launch
        if (resetAtLaunch) {
            return true;
        }

        // It's a new version
        if (resetAtVersion && !getBoolean(context, versionName)) {
            return true;
        }

        return shouldReset;
    }

    public static Boolean getBoolean(Context context, String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] getBoolean (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        return settings.getBoolean(key, false);
    }

    public static void editBoolean(Context context, String key, Boolean value) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] editBoolean (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Float getFloat(Context context, String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] getFloat (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        return settings.getFloat(key, -1.0f);
    }

    public static void editFloat(Context context, String key, Float value) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] editFloat (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void editLong(Context context, String key, long value) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] editLong (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] getLong (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        return settings.getLong(key, -1);
    }

    public static int getInt(Context context, String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] getInt (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        return settings.getInt(key, -1);
    }

    public static void editInt(Context context, String key, int value) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] editInt (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] getString (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        return settings.getString(key, "");
    }

    public static void editString(Context context, String key, String value) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] editString (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] getStringSet (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        return settings.getStringSet(key, new HashSet<String>());
    }

    public static void editStringSet(Context context, String key, Set<String> value) {
        if (Strings.isNullOrEmpty(key)) {
            throw new RuntimeException("[SharedPrefs] editStringSet (key=" + key + ')');
        }

        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     * Remove all the preferences
     */
    public static void clear(Context context) {
        SharedPreferences settings = getPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public static SharedPreferences getPrefs(Context context) {
        //return uiContext.getSharedPreferences(app, Context.MODE_MULTI_PROCESS);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences settings = getPrefs(context);
        return settings.contains(key);
    }

}
