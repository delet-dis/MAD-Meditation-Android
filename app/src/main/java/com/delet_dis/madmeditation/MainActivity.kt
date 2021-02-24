package com.delet_dis.madmeditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.databinding.ActivityMainBinding
import com.delet_dis.madmeditation.recyclerView.FeelingsAdapter
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.SharedPrefsHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.*
import com.delet_dis.madmeditation.recyclerView.QuotesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

  private lateinit var userAvatar: ImageView
  private lateinit var userNickName: TextView

  private lateinit var feelingsView: RecyclerView

  private lateinit var quotesView: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root

    setContentView(view)

    userAvatar = binding.userImage
    userNickName = binding.welcomeText

    feelingsView = binding.feelingsRecycler
    feelingsView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    quotesView = binding.quotesRecycler
    quotesView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    val loginResponse =
      intent.extras?.getParcelable<LoginResponse>(ConstantsHelper.loginResponseParcelableName)

    var processingLoginResponse: LoginResponse?

    if (loginResponse == null) {
      val loginRequest = SharedPrefsHelper.getLoginData(applicationContext)

      Common.retrofitService.postLoginData(loginRequest)
        .enqueue(object : Callback<LoginResponse> {

          override fun onResponse(
            call: Call<LoginResponse>,
            response: Response<LoginResponse>
          ) {
            processingLoginResponse = response.body()
            displayUserInfo(processingLoginResponse)
          }

          override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(
              applicationContext,
              getString(R.string.networkErrorMessage),
              Toast.LENGTH_SHORT
            ).show()
          }

        })
    } else {
      processingLoginResponse = loginResponse
      displayUserInfo(processingLoginResponse)
    }

    Common.retrofitService.getFeelingsData()
      .enqueue(object : Callback<FeelingsResponse> {
        override fun onResponse(
          call: Call<FeelingsResponse>,
          response: Response<FeelingsResponse>
        ) {
          val processingFeelingsList: List<Feeling> =
            response.body()!!.data.sortedWith(compareBy { it.position })

          feelingsView.adapter = FeelingsAdapter(processingFeelingsList)
        }

        override fun onFailure(call: Call<FeelingsResponse>, t: Throwable) {
          Toast.makeText(
            applicationContext,
            getString(R.string.networkErrorMessage),
            Toast.LENGTH_SHORT
          ).show()
        }

      })

    Common.retrofitService.getQuotesData()
      .enqueue(object : Callback<QuotesResponse> {
        override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
          val processingQuotesList: List<Quote> = response.body()!!.data
          quotesView.adapter = QuotesAdapter(processingQuotesList)
        }

        override fun onFailure(call: Call<QuotesResponse>, t: Throwable) {
          Toast.makeText(
            applicationContext,
            getString(R.string.networkErrorMessage),
            Toast.LENGTH_SHORT
          ).show()
        }

      })

  }

  private fun displayUserInfo(processingLoginResponse: LoginResponse?) {
    Glide.with(applicationContext)
      .load(processingLoginResponse?.avatar)
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(userAvatar)

    userNickName.text =
      String.format(getString(R.string.welcomeTextText, processingLoginResponse?.nickName))
  }
}