package com.delet_dis.madmeditation.helpers

import android.app.Activity
import android.content.Intent
import com.delet_dis.madmeditation.MainActivity
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RetrofitHelper {

  fun postLoginData(activity: Activity, loginRequest: LoginRequest) {
    Common.retrofitService.postLoginData(loginRequest)
      .enqueue(object : Callback<LoginResponse> {

        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
          if (response.errorBody() !== null) {
            AlertDialogHelper.buildAlertDialog(
              activity, R.string.alertDialogLoginFailedMessage
            )
          } else {
            SharedPreferencesHelper(activity.applicationContext).setLoginState(true)

            SharedPreferencesHelper(activity.applicationContext).setLoginData(
              loginRequest.email,
              loginRequest.password
            )

            val processingIntent = Intent(activity, MainActivity::class.java)
            processingIntent.putExtra(ConstantsHelper.loginResponseParcelableName, response.body())
            activity.startActivity(processingIntent)
            activity.finish()
          }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          AlertDialogHelper.buildAlertDialog(
            activity, R.string.networkErrorMessage
          )
        }
      })
  }

}