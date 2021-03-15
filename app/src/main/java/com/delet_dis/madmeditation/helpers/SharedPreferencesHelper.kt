package com.delet_dis.madmeditation.helpers

import android.content.Context
import android.content.SharedPreferences
import com.delet_dis.madmeditation.model.LoginRequest

class SharedPreferencesHelper(val context: Context) {

  private fun getSharedPreferences(): SharedPreferences {
    return context.getSharedPreferences(ConstantsHelper.appSettings, Context.MODE_PRIVATE)
  }

  fun isLoggedIn(): Boolean {
    return getSharedPreferences().getBoolean(ConstantsHelper.loginState, false)
  }

  fun setLoginState(loggedIn: Boolean) {
    getSharedPreferences().edit().putBoolean(ConstantsHelper.loginState, loggedIn).apply()
  }

  fun setLoginData(email: String, password: String) {
    getSharedPreferences().edit().putString(ConstantsHelper.userEmail, email).apply()
    getSharedPreferences().edit().putString(ConstantsHelper.userPassword, password).apply()
  }

  fun getLoginData(): LoginRequest {
    return LoginRequest(
      getSharedPreferences().getString(ConstantsHelper.userEmail, "null") ?: "null",
      getSharedPreferences().getString(ConstantsHelper.userPassword, "null") ?: "null"
    )
  }

  fun getEmail(): String? {
    return getSharedPreferences().getString(ConstantsHelper.userEmail, null)
  }

  fun clearLoginData() {
    setLoginState(false)
    getSharedPreferences().edit().remove(ConstantsHelper.userPassword).apply()
  }
}