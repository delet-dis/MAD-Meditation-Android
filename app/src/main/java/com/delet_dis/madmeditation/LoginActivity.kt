package com.delet_dis.madmeditation

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
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

  private lateinit var emailInputField: EditText
  private lateinit var passwordInputField: EditText

  private lateinit var loginButton: Button
  private lateinit var registerButton: Button


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowHelper.setWindowNoLimits(this)

    val view = createViewBinding()

    setContentView(view)

    findViewElements()

    fillEmailFieldIfEmailIsNotNull()

    registerLoginButtonOnclick()

    registerRegisterButtonOnclick()

  }

  private fun fillEmailFieldIfEmailIsNotNull() {
    if (SharedPreferencesHelper.getEmail(applicationContext) != null) {
      emailInputField.setText(SharedPreferencesHelper.getEmail(applicationContext))
    }
  }

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityLoginBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun registerRegisterButtonOnclick() {
    registerButton.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }
  }

  private fun registerLoginButtonOnclick() {
    loginButton.setOnClickListener {
      checkCorrectnessOfFields()
    }
  }

  private fun findViewElements() {
    emailInputField = binding.emailInputField
    passwordInputField = binding.passwordInputField

    loginButton = binding.loginButton
    registerButton = binding.noAccountHintRegistration
  }

  private fun checkCorrectnessOfFields() {
    if (isEmailCorrect(emailInputField.text.toString()) &&
      passwordInputField.text.toString().isNotBlank()
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
            SharedPreferencesHelper.setLoginState(applicationContext, true)

            SharedPreferencesHelper.setLoginData(
              applicationContext,
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
    emailInputField.text.toString(),
    passwordInputField.text.toString()
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