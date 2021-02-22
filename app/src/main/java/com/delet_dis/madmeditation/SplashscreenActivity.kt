package com.delet_dis.madmeditation

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.madmeditation.helpers.SharedPrefsHelper
import com.delet_dis.madmeditation.helpers.WindowHelper

class SplashscreenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splashscreen)


    val handler = Handler(Looper.getMainLooper())

    lateinit var processingIntent: Intent

    handler.postDelayed({
      processingIntent = if (SharedPrefsHelper.isLoggedIn(applicationContext)) {
        Intent(this, MainActivity::class.java)
      } else {
        Intent(this, OnboardingActivity::class.java)
      }
      startActivity(processingIntent)
      finish()
    }, 1500)

  }


}