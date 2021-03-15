package com.delet_dis.madmeditation

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.delet_dis.madmeditation.databinding.ActivityLoginBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper
import com.delet_dis.madmeditation.helpers.WindowHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowHelper.setWindowNoLimits(this)

    setContentView(createViewBinding())

    fillEmailFieldIfEmailIsNotNull()

    registerLoginButtonOnclick()

    registerRegisterButtonOnclick()
  }

  private fun fillEmailFieldIfEmailIsNotNull() {
    if (SharedPreferencesHelper(applicationContext).getEmail() != null) {
      binding.emailInputField.setText(SharedPreferencesHelper(applicationContext).getEmail())
    }
  }

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityLoginBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun registerRegisterButtonOnclick() {
    binding.noAccountHintRegistration.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }
  }

  private fun registerLoginButtonOnclick() {
    binding.loginButton.setOnClickListener {
      checkCorrectnessOfFields()
    }
  }

  private fun checkCorrectnessOfFields() {
    if (isEmailCorrect(binding.emailInputField.text.toString()) &&
      binding.passwordInputField.text.toString().isNotBlank()
    ) {

      val loginRequest = makeLoginRequest()

      postLoginData(loginRequest)

    } else {
      buildAlertDialog(R.string.alertDialogDataIncorrectMessage)
    }
  }

  private fun postLoginData(loginRequest: LoginRequest) {
    Common.retrofitService.postLoginData(loginRequest)
      .enqueue(object : Callback<LoginResponse> {

        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
          if (response.errorBody() !== null) {
            buildAlertDialog(R.string.alertDialogLoginFailedMessage)
          } else {
            SharedPreferencesHelper(applicationContext).setLoginState(true)

            SharedPreferencesHelper(applicationContext).setLoginData(
              loginRequest.email,
              loginRequest.password
            )

            val processingIntent = Intent(this@LoginActivity, MainActivity::class.java)
            processingIntent.putExtra(ConstantsHelper.loginResponseParcelableName, response.body())
            startActivity(processingIntent)
            finish()
          }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          buildAlertDialog(R.string.networkErrorMessage)
        }
      })
  }

  private fun makeLoginRequest() = LoginRequest(
    binding.emailInputField.text.toString(),
    binding.passwordInputField.text.toString()
  )

  private fun isEmailCorrect(processingEmail: String): Boolean {
    return !TextUtils.isEmpty(processingEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(
      processingEmail
    ).matches()
  }

  private fun buildAlertDialog(stringResourceId: Int) {
    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.alertDialogLoginFailedTitle))
      .setMessage(getString(stringResourceId))
      .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
      .show()
  }
}