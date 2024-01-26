package com.swidch.otacauth.Utils.sharedPreference

import android.content.Context

object SharedPreferenceManager {

    // String
    fun setStringValue(context: Context, id: String, value: String?) {
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.setStringPreference(
            context,
            id,
            value
        )
    }

    fun getStringValue(context: Context, id: String): String? {
        return com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.getStringPreference(
            context,
            id
        )
    }

    // Long
    fun setLongValue(context: Context, id: String, value: Long) {
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.setLongPreference(
            context,
            id,
            value
        )
    }

    fun getLongValue(context: Context, id: String, defaultValue: Long): Long {
        return com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.getLongPreference(
            context,
            id,
            defaultValue
        )
    }

    // Boolean
    fun setBooleanValue(context: Context, id: String, value: Boolean) {
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.setBooleanPreference(
            context,
            id,
            value
        )
    }

    fun getBooleanValue(context: Context, id: String, defaultValue: Boolean): Boolean {
        return com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.getBooleanPreference(
            context,
            id,
            defaultValue
        )
    }

    fun setQRdata(context: Context, item: String?) {
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.setStringPreference(
            context,
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_REGIST_QR,
            item
        )
    }

    fun getQRdata(context: Context): String? {
        return com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.getStringPreference(
            context,
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_REGIST_QR
        )
    }

    fun getAccountList(context:Context):String? {
        return com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.getStringPreference(
            context,
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_ACCOUNT_LIST
        )
    }

    fun setAccountList(context:Context, item: String?) {
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.setStringPreference(
            context,
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_ACCOUNT_LIST,
            item
        )
    }

    fun getChangeAccountList(context:Context):String? {
        return com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.getStringPreference(
            context,
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_CHANGE_ACCOUNT_LIST
        )
    }

    fun setChangeAccountList(context:Context, item: String?) {
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceUtil.setStringPreference(
            context,
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_CHANGE_ACCOUNT_LIST,
            item
        )
    }

}