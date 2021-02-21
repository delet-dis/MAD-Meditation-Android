package com.delet_dis.madmeditation.http.common

import com.delet_dis.madmeditation.http.retrofit.RetrofitClient
import com.delet_dis.madmeditation.http.retrofit.RetrofitServices

object Common {
  private const val BASE_URL = "http://mskko2021.mad.hakta.pro/api/"
  val retrofitService: RetrofitServices
    get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}