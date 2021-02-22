package com.delet_dis.madmeditation

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.madmeditation.databinding.ActivityLoginBinding
import com.delet_dis.madmeditation.helpers.WindowHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding

  private lateinit var emailEditText: EditText
  private lateinit var passwordEditText: EditText

  private lateinit var loginButton: Button
  private lateinit var registerTextView: TextView

  private var gson: Gson = Gson()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    WindowHelper.setWindowNoLimits(this)

    binding = ActivityLoginBinding.inflate(layoutInflater)
    val view = binding.root

    setContentView(view)

    emailEditText = binding.emailInputField
    passwordEditText = binding.passwordInputField

    loginButton = binding.loginButton
    registerTextView = binding.noAccountHintRegistration

    loginButton.setOnClickListener {
      checkCorrectnessOfFields()
    }

    registerTextView.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }

  }

  private fun checkCorrectnessOfFields() {
    if (isEmailCorrect(emailEditText.text.toString()) &&
      passwordEditText.text.toString().isNotBlank()
    ) {

      val loginRequest = LoginRequest(
        emailEditText.text.toString(),
        passwordEditText.text.toString()
      )

      val retrofitService = Common.retrofitService

      retrofitService.postLoginData(loginRequest)
        .enqueue(object : retrofit2.Callback<LoginResponse> {
          override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            Log.d("test", response.body()?.error.toString())
          }

          override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.d("test", "test")
          }
        })


    } else {
      AlertDialog.Builder(this)
        .setTitle(getString(R.string.alertDialogLoginFailedTitle))
        .setMessage(getString(R.string.alertDialogLoginFailedMessage))
        .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        .show()
    }
  }

  private fun isEmailCorrect(processingEmail: String): Boolean {
    return !TextUtils.isEmpty(processingEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(processingEmail).matches()
  }
}