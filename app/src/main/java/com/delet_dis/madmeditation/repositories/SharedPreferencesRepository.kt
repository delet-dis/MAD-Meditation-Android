package com.delet_dis.madmeditation.repositories

import android.content.Context
import android.content.SharedPreferences
import com.delet_dis.madmeditation.model.LoginRequest

class SharedPreferencesRepository(val context: Context) {

  private fun getSharedPreferences(): SharedPreferences {
    return context.getSharedPreferences(ConstantsRepository.appSettings, Context.MODE_PRIVATE)
  }

  fun isLoggedIn(): Boolean {
    return getSharedPreferences().getBoolean(ConstantsRepository.loginState, false)
  }

  fun setLoginState(loggedIn: Boolean) {
    getSharedPreferences().edit().putBoolean(ConstantsRepository.loginState, loggedIn).apply()
  }

  fun setLoginData(email: String, password: String) {
    getSharedPreferences().edit().putString(ConstantsRepository.userEmail, email).apply()
    getSharedPreferences().edit().putString(ConstantsRepository.userPassword, password).apply()
  }

  fun getLoginData(): LoginRequest {
    return LoginRequest(
      getSharedPreferences().getString(ConstantsRepository.userEmail, "null") ?: "null",
      getSharedPreferences().getString(ConstantsRepository.userPassword, "null") ?: "null"
    )
  }

  fun getEmail(): String? {
    return getSharedPreferences().getString(ConstantsRepository.userEmail, null)
  }

  fun clearLoginData() {
    setLoginState(false)
    getSharedPreferences().edit().remove(ConstantsRepository.userPassword).apply()
  }
}