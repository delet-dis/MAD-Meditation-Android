package com.delet_dis.madmeditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.delet_dis.madmeditation.helpers.WindowHelper

class OnboardingActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_onboarding)

    WindowHelper.setWindowTransparent(this)
  }
}