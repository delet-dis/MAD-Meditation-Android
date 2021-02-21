package com.delet_dis.madmeditation

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.madmeditation.databinding.ActivityLoginBinding
import com.delet_dis.madmeditation.helpers.PatternHelper
import com.delet_dis.madmeditation.helpers.WindowHelper
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding

  private lateinit var emailEditText: EditText
  private lateinit var passwordEditText: EditText

  private lateinit var loginButton: Button
  private lateinit var registerTextView: TextView

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

    registerTextView.setOnClickListener {
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }

  }

  fun checkCorrectnessOfFields(){
    if (isEmailCorrect(emailEditText.text.toString()) &&
      passwordEditText.text.toString().isNotBlank()
    ) {

    } else {
      AlertDialog.Builder(this)
        .setTitle(getString(R.string.alertDialogLoginFailedTitle))
        .setMessage(getString(R.string.alertDialogLoginFailedMessage))
        .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        .show()
    }
  }

  private fun isEmailCorrect(processingEmail: String): Boolean {
    val pattern: Pattern = Pattern.compile(PatternHelper.emailRegex)

    val matcher: Matcher = pattern.matcher(processingEmail)
    return matcher.find()
  }
}