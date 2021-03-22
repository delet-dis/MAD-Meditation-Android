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

  fun postLoginData(
    activity: Activity,
    loginRequest: LoginRequest,
    afterCallFunctionOnResponse: (Response<LoginResponse>) -> Unit,
    afterCallFunctionOnFailure: () -> Unit
  ) {
    Common.retrofitService.postLoginData(loginRequest)
      .enqueue(object : Callback<LoginResponse> {

        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
          afterCallFunctionOnResponse(response)
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          afterCallFunctionOnFailure()
        }
      })
  }

}