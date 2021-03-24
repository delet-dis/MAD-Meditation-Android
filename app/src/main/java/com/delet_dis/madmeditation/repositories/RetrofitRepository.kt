package com.delet_dis.madmeditation.repositories

import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.FeelingsResponse
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import com.delet_dis.madmeditation.model.QuotesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.KFunction1

object RetrofitRepository {

  fun postLoginData(
    loginRequest: LoginRequest,
    afterCallFunctionOnResponse: KFunction1<Response<LoginResponse>, Unit>,
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

  fun getQuotesData(
    afterCallFunctionOnResponse: KFunction1<Response<QuotesResponse>, Unit>,
    afterCallFunctionOnFailure: () -> Unit
  ) {
    Common.retrofitService.getQuotesData()
      .enqueue(object : Callback<QuotesResponse> {
        override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
          afterCallFunctionOnResponse(response)
        }

        override fun onFailure(call: Call<QuotesResponse>, t: Throwable) {
          afterCallFunctionOnFailure()
        }

      })
  }

  fun getFeelingsData(
    afterCallFunctionOnResponse: (Response<FeelingsResponse>) -> Unit,
    afterCallFunctionOnFailure: () -> Unit
  ) {
    Common.retrofitService.getFeelingsData()
      .enqueue(object : Callback<FeelingsResponse> {
        override fun onResponse(
          call: Call<FeelingsResponse>,
          response: Response<FeelingsResponse>
        ) {
          afterCallFunctionOnResponse(response)
        }

        override fun onFailure(call: Call<FeelingsResponse>, t: Throwable) {
          afterCallFunctionOnFailure()
        }

      })
  }
}