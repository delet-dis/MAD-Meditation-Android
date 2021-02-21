package com.delet_dis.madmeditation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.madmeditation.helpers.WindowHelper

class SplashscreenActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splashscreen)

    WindowHelper.setWindowTransparent(this)

    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
      val intent = Intent(this, OnboardingActivity::class.java)
      startActivity(intent)
      finish()
    }, 1500)

  }

}