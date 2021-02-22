package com.delet_dis.madmeditation

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.delet_dis.madmeditation.databinding.ActivityLoginBinding
import com.delet_dis.madmeditation.helpers.SharedPrefsHelper
import com.delet_dis.madmeditation.helpers.WindowHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding

  private lateinit var emailEditText: EditText
  private lateinit var passwordEditText: EditText

  private lateinit var loginButton: Button
  private lateinit var registerTextView: TextView


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowHelper.setWindowNoLimits(this)

    val view = createViewBinding()

    setContentView(view)

    findViewElements()

    registerLoginButtonOnclick()

    registerRegisterTextViewOnclick()

  }

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityLoginBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun registerRegisterTextViewOnclick() {
    registerTextView.setOnClickListener {
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
    emailEditText = binding.emailInputField
    passwordEditText = binding.passwordInputField

    loginButton = binding.loginButton
    registerTextView = binding.noAccountHintRegistration
  }

  private fun checkCorrectnessOfFields() {
    if (isEmailCorrect(emailEditText.text.toString()) &&
      passwordEditText.text.toString().isNotBlank()
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
            SharedPrefsHelper.setLoginState(applicationContext, true)

            val processingIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(processingIntent)
            finish()
          }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          buildAlertDialog(R.string.alertDialogNetworkErrorMessage)
        }
      })
  }

  private fun makeLoginRequest() = LoginRequest(
    emailEditText.text.toString(),
    passwordEditText.text.toString()
  )

  private fun isEmailCorrect(processingEmail: String): Boolean {
    return !TextUtils.isEmpty(processingEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(
      processingEmail
    ).matches()
  }

  private fun buildAlertDialog(stringResourceId: Int) {
    AlertDialog.Builder(this)
      .setTitle(getString(R.string.alertDialogLoginFailedTitle))
      .setMessage(getString(stringResourceId))
      .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
      .show()
  }
}