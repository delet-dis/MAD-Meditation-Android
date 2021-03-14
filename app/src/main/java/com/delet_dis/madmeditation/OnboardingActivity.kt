package com.delet_dis.madmeditation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.delet_dis.madmeditation.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
  private lateinit var binding: ActivityOnboardingBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(createViewBinding())

    registerRegisterButtonOnclick()

    registerLoginButtonOnclick()
  }

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityOnboardingBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun registerLoginButtonOnclick() {
    binding.loginButton.setOnClickListener {
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
    }
  }

  private fun registerRegisterButtonOnclick() {
    binding.noAccountHintRegistration.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }
  }
}