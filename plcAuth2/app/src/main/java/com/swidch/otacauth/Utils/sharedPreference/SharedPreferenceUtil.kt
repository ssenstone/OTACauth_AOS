package com.swidch.otacauth.Utils.sharedPreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log

object SharedPreferenceUtil {
    val name = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.PREFERENCE
    /**
     * Helper method to show all shared preferences from [SharedPreferences].
     *
     * @param context a [Context] object.
     */
    fun showAll(context: Context) {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            val allEntries = preferences.all
            for ((key, value) in allEntries) {
                Log.d("map values", key + ": " + (value?.toString() ?: "null"))
            }
        }
    }

    /**
     * Helper method to clear all shared preferences from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @return The value from shared preferences, or null if the value could not be read.
     */
    fun clearAll(context: Context): Boolean {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.clear()
            return editor.commit()
        }
        return false
    }


    /**
     * Helper method to retrieve a String value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    fun getStringPreference(context: Context, key: String): String? {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        var value: String? = null
        if (preferences != null) {
            value = preferences.getString(key, null)
        }
        return value
    }

    /**
     * Helper method to write a String value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     */
    fun setStringPreference(context: Context, key: String, value: String?) {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null && !TextUtils.isEmpty(key)) {
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    /**
     * Helper method to retrieve a float value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getFloatPreference(context: Context, key: String, defaultValue: Float): Float {
        var value = defaultValue
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write a float value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     */
    fun setFloatPreference(context: Context, key: String, value: Float) {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putFloat(key, value)
            editor.apply()
        }
    }

    /**
     * Helper method to retrieve a long value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getLongPreference(context: Context, key: String, defaultValue: Long): Long {
        var value = defaultValue
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write a long value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     */
    fun setLongPreference(context: Context, key: String, value: Long) {
        val preferences: SharedPreferences?
        try {
            preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return
        }

        if (preferences != null) {
            val editor = preferences.edit()
            editor.putLong(key, value)
            editor.apply()
        }
    }

    /**
     * Helper method to retrieve an integer value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getIntegerPreference(context: Context, key: String, defaultValue: Int): Int {
        var value = defaultValue
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write an integer value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     */
    fun setIntegerPreference(context: Context, key: String, value: Int) {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putInt(key, value)
            editor.apply()
        }
    }

    /**
     * Helper method to retrieve a boolean value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getBooleanPreference(context: Context, key: String, defaultValue: Boolean): Boolean {
        var value = defaultValue
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write a boolean value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     */
    fun setBooleanPreference(context: Context, key: String, value: Boolean) {
        val preferences = context.getSharedPreferences(com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.name, MODE_PRIVATE)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }
    }
}