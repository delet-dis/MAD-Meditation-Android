package com.delet_dis.madmeditation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.databinding.FragmentMainScreenBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.IntentHelper
import com.delet_dis.madmeditation.helpers.ToastHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.*
import com.delet_dis.madmeditation.recyclerViewAdapters.FeelingsAdapter
import com.delet_dis.madmeditation.recyclerViewAdapters.QuotesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainScreenFragment : Fragment() {

  private lateinit var userAvatar: ImageView
  private lateinit var welcomeTextWithUserName: TextView

  private lateinit var hamburgerImage: ImageView

  private lateinit var feelingsRecycler: RecyclerView

  private lateinit var quotesRecycler: RecyclerView

  private lateinit var binding: FragmentMainScreenBinding

  private var loginResponse: LoginResponse? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return if (savedInstanceState == null) {
      binding = FragmentMainScreenBinding.inflate(layoutInflater)

      loginResponse = requireArguments()
        .getParcelable(ConstantsHelper.loginResponseParcelableName)

      binding.root
    } else {
      view
    }


  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState == null) {

      userAvatar = binding.userAvatar
      welcomeTextWithUserName = binding.welcomeHeaderWithUserName
      hamburgerImage = binding.hamgurgerImage

      feelingsRecycler = binding.feelingsRecycler
      feelingsRecycler.layoutManager = LinearLayoutManager(
        activity,
        LinearLayoutManager.HORIZONTAL,
        false
      )

      quotesRecycler = binding.quotesRecycler
      quotesRecycler.layoutManager = LinearLayoutManager(
        activity,
        LinearLayoutManager.VERTICAL,
        false
      )

      hamburgerImage.setOnClickListener {
        this.context?.let { it1 -> IntentHelper.startMenuActivity(it1) }
      }

      displayUserInfo(loginResponse)

      Common.retrofitService.getFeelingsData()
        .enqueue(object : Callback<FeelingsResponse> {
          override fun onResponse(
            call: Call<FeelingsResponse>,
            response: Response<FeelingsResponse>
          ) {
            val processingFeelingsList: List<Feeling> =
              response.body()!!.data.sortedWith(compareBy { it.position })

            feelingsRecycler.adapter = FeelingsAdapter(processingFeelingsList)
          }

          override fun onFailure(call: Call<FeelingsResponse>, t: Throwable) {
            activity?.let {
              ToastHelper.createErrorToast(
                it.applicationContext,
                R.string.networkErrorMessage
              )
            }
          }

        })

      Common.retrofitService.getQuotesData()
        .enqueue(object : Callback<QuotesResponse> {
          override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
            val processingQuotesList: List<Quote> = response.body()!!.data
            quotesRecycler.adapter = QuotesAdapter(processingQuotesList)
          }

          override fun onFailure(call: Call<QuotesResponse>, t: Throwable) {
            activity?.let {
              ToastHelper.createErrorToast(
                it.applicationContext,
                R.string.networkErrorMessage
              )
            }
          }

        })
    }

  }

  private fun displayUserInfo(processingLoginResponse: LoginResponse?) {
    activity?.let {
      Glide.with(it)
        .load(processingLoginResponse?.avatar)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(userAvatar)
    }

    welcomeTextWithUserName.text =
      String.format(getString(R.string.welcomeTextText, processingLoginResponse?.nickName))
  }

}