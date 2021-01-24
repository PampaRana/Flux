package com.velectico.rbm.utils

import android.content.Context
import android.content.SharedPreferences
object SharedPreferencesClass {
    var prefs: SharedPreferences? = null
    var context: Context? = null
    fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun insertData(context: Context, key: String?, value: String?) {
        val editor = getPrefs(context).edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun insertStringSet(
        context: Context,
        key: String?,
        value: Set<String?>?
    ) {
        getPrefs(context).edit().putStringSet(key, value).commit()
    }

    fun retriveData(context: Context, key: String?): String? {
        return getPrefs(context).getString(key, "no_data_found")
    }

    fun getStringSet(
        context: Context,
        key: String?
    ): Set<String?>? {
        return getPrefs(context).getStringSet(key, null)
    }

    fun deleteData(context: Context?, key: String?) {
        val editor = getPrefs(context!!).edit()
        editor.remove(key)
        editor.commit()
    }

    fun clearData(context: Context) {
        val editor = getPrefs(context).edit()
        editor.clear()
        editor.commit()
    }
}