package com.bestoffers.enjoeepharmacy

import android.content.Context
import android.content.Intent
import com.bestoffers.enjoeepharmacy.Activity.HomeActivity
import com.bestoffers.enjoeepharmacy.Activity.LoginActivity
import java.util.HashMap
import android.content.SharedPreferences as SharedPreferences

class SessionManager(context: Context) {

    var PRIVATE_MODE = 0
    private val PREF_NAME = "LOGIN"
    private val LOGIN = "IS_LOGIN"
    val USER_ID = "user_id"
    val USER_NAME = "user_name"
    val USER_PHONE = "user_phone"
    val USER_EMAIL = "user_email"
    val USER_DEVICE_ID = "user_device_id"
    val USER_DEVICE_TOKEN = "user_device_token"
    val USER_AUTHORIZATION = "user_authorization"

    var context = context
    var sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor = sharedPreferences?.edit()

//    public fun SessionManager() {
//        this.context = context
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
//        editor = sharedPreferences?.edit()
//    }

    fun CreateSession(
        user_id: String?,
        user_Name: String?,
        user_phone: String?,
        user_email: String?,
//        device_id: String?,
        device_token: String?,
        user_auth: String?
    ) {
        editor!!.putBoolean(LOGIN, true)
        editor!!.putString(USER_EMAIL, user_email)
        editor!!.putString(USER_PHONE, user_phone)
        editor!!.putString(USER_NAME, user_Name)
        editor!!.putString(USER_ID, user_id)
//        editor!!.putString(USER_DEVICE_ID, device_id)
        editor!!.putString(USER_DEVICE_TOKEN, device_token)
        editor!!.putString(USER_AUTHORIZATION, user_auth)
        editor!!.apply()
    }

    fun isLogin(): Boolean {
//        if (sharedPreferences != null) {
            return sharedPreferences!!.getBoolean(LOGIN, false)
//        } else {
//            return false
//        }
    }

    fun checkLogin(): Boolean {
        if (!isLogin()) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
            return true
        }
        return false
    }

    fun getuserDetails(): HashMap<String, String> {
        val user = HashMap<String, String>()
        user[USER_AUTHORIZATION] = sharedPreferences!!.getString(USER_AUTHORIZATION, null).toString()
        user[USER_DEVICE_TOKEN] = sharedPreferences!!.getString(USER_DEVICE_TOKEN, null).toString()
//        user[USER_DEVICE_ID] = sharedPreferences!!.getString(USER_DEVICE_ID, null)
        user[USER_EMAIL] = sharedPreferences!!.getString(USER_EMAIL, null).toString()
        user[USER_PHONE] = sharedPreferences!!.getString(USER_PHONE, null).toString()
        user[USER_NAME] = sharedPreferences!!.getString(USER_NAME, null).toString()
        user[USER_ID] = sharedPreferences!!.getString(USER_ID, null).toString()
        return user
    }

    fun logout() {
        editor!!.clear()
        editor!!.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(intent)
//        ((MainActivity)context).finish();
    }

}