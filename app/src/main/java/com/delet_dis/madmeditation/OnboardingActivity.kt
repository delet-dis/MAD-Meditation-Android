package com.delet_dis.madmeditation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.delet_dis.madmeditation.helpers.WindowHelper
import com.delet_dis.madmeditation.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
  private lateinit var binding: ActivityOnboardingBinding

  private lateinit var loginButton: Button
  private lateinit var registerTextView: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityOnboardingBinding.inflate(layoutInflater)
    val view = binding.root

    setContentView(view)

    WindowHelper.setWindowTransparent(this)

    loginButton = binding.loginButton
    registerTextView = binding.noAccountHintRegistration

    registerTextView.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }
  }
}