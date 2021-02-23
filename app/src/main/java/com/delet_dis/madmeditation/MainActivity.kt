package com.delet_dis.madmeditation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.SharedPrefsHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val loginResponse =
      intent.extras?.getParcelable<LoginResponse>(ConstantsHelper.loginResponseParcelableName)

    if (loginResponse == null) {

      val loginRequest = SharedPrefsHelper.getLoginData(applicationContext)

      Common.retrofitService.postLoginData(loginRequest)
        .enqueue(object : Callback<LoginResponse> {

          override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            SharedPrefsHelper.setLoginState(applicationContext, true)

            val processingIntent = Intent(this@LoginActivity, MainActivity::class.java)
            processingIntent.putExtra(ConstantsHelper.loginResponseParcelableName, response.body())
            startActivity(processingIntent)
            finish()

          }

          override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(
              applicationContext,
              getString(R.string.networkErrorMessage),
              Toast.LENGTH_SHORT
            ).show()
          }
        })
    }
  }
}