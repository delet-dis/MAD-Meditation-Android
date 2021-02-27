package com.delet_dis.madmeditation.helpers

import android.content.Context
import android.content.SharedPreferences
import com.delet_dis.madmeditation.model.LoginRequest

object SharedPreferencesHelper {

  private fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(ConstantsHelper.appSettings, Context.MODE_PRIVATE)
  }

  fun getValue(context: Context, setting: String): String? {
    return getSharedPreferences(context).getString(setting, null)
  }

  fun setValue(context: Context, setting: String, newValue: String?) {
    val editor = getSharedPreferences(context).edit()
    editor.putString(setting, newValue)
    editor.apply()
  }

  fun isLoggedIn(context: Context): Boolean {
    return getSharedPreferences(context).getBoolean(ConstantsHelper.loginState, false)
  }

  fun setLoginState(context: Context, loggedIn: Boolean) {
    getSharedPreferences(context).edit().putBoolean(ConstantsHelper.loginState, loggedIn).apply()
  }

  fun setLoginData(context: Context, email: String, password: String) {
    getSharedPreferences(context).edit().putString(ConstantsHelper.userEmail, email).apply()
    getSharedPreferences(context).edit().putString(ConstantsHelper.userPassword, password).apply()
  }

  fun getLoginData(context: Context): LoginRequest {
    return LoginRequest(
      getSharedPreferences(context).getString(ConstantsHelper.userEmail, "null") ?: "null",
      getSharedPreferences(context).getString(ConstantsHelper.userPassword, "null") ?: "null"
    )
  }

  fun getEmail(context: Context): String? {
    return getSharedPreferences(context).getString(ConstantsHelper.userEmail, null)
  }

  fun clearLoginData(context: Context) {
    setLoginState(context, false)
    getSharedPreferences(context).edit().remove(ConstantsHelper.userPassword).apply()
  }
}