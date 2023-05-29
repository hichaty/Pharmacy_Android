package com.bestoffers.enjoeepharmacy.Utils

import android.content.Context
import android.content.SharedPreferences
import com.bestoffers.enjoeepharmacy.customViews.MyApplication

class SharedPreferenceUtility {

    private var sharedPreferences: SharedPreferences
    private val SHARED_PREFS_NAME = "EnjoeePharmacy"

    companion object{
        private var instance: SharedPreferenceUtility? = null

        @Synchronized
        fun getInstance(): SharedPreferenceUtility {
            if (instance == null) {
                instance = SharedPreferenceUtility()
            }
            return instance!!
        }
    }

    constructor(){
        instance = this;
        sharedPreferences = MyApplication.instance.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    fun delete(key: String) {
        if (sharedPreferences.contains(key)) {
            getEditor().remove(key).commit()
        }
    }

    fun save(key: String, value: Any?) {
        val editor = getEditor()
        if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Int) {
            editor.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float?)!!)
        } else if (value is Long) {
            editor.putLong(key, (value as Long?)!!)
        } else if (value is String) {
            editor.putString(key, value as String?)
        } else if (value is Enum<*>) {
            editor.putString(key, value.toString())
        } else if (value != null) {
            throw RuntimeException("Attempting to save non-supported preference")
        }

        editor.commit()
    }

    operator fun <T> get(key: String): T {
        return sharedPreferences.all[key] as T
    }

    operator fun <T> get(key: String, defValue: T): T {
        val returnValue = sharedPreferences.all[key] as T
        return returnValue ?: defValue
    }

    fun has(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    fun clearSharedPreferences() {
        val editor = getEditor()
        editor.clear()
        editor.commit()
    }

}