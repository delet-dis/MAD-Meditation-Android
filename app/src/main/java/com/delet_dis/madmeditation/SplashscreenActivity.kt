package com.delet_dis.madmeditation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper

class SplashscreenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splashscreen)


    val handler = Handler(Looper.getMainLooper())

    var processingIntent: Intent

    handler.postDelayed({
      processingIntent = if (SharedPreferencesHelper.isLoggedIn(applicationContext)) {
        Intent(this, MainActivity::class.java)
      } else {
        Intent(this, OnboardingActivity::class.java)
      }
      startActivity(processingIntent)
      finish()
    }, 1500)
  }
}