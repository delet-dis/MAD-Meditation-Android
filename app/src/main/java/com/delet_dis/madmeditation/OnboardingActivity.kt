package com.delet_dis.madmeditation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.delet_dis.madmeditation.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
  private lateinit var binding: ActivityOnboardingBinding

  private lateinit var loginButton: Button
  private lateinit var registerButton: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = createViewBinding()

    setContentView(view)

    findViewElements()

    registerRegisterButtonOnclick()

    registerLoginButtonOnclick()
  }

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityOnboardingBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun registerLoginButtonOnclick() {
    loginButton.setOnClickListener {
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
    }
  }

  private fun registerRegisterButtonOnclick() {
    registerButton.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }
  }

  private fun findViewElements() {
    loginButton = binding.loginButton
    registerButton = binding.noAccountHintRegistration
  }
}