package com.delet_dis.madmeditation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class SplashscreenActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splashscreen)

    setWindowTransparency()

    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
      finish()
    }, 1500)

  }

  private fun setWindowTransparency() {
    val window = window

    window.statusBarColor = Color.TRANSPARENT
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
  }
}