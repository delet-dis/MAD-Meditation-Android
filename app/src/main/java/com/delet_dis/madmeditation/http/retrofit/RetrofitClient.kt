package com.delet_dis.madmeditation.http.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class RetrofitClient {

  companion object {
    private var INSTANCE: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
      if (INSTANCE == null) {
        INSTANCE = Retrofit.Builder()
          .baseUrl(baseUrl)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
      }
      return INSTANCE!!
    }
  }
}