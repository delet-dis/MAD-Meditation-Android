package com.delet_dis.madmeditation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
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

  private lateinit var binding: FragmentMainScreenBinding

  private lateinit var parentActivityCallback: ActivityCallback

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

  override fun onAttach(context: Context) {
    super.onAttach(context)

    parentActivityCallback = context as ActivityCallback
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState == null) {

      setHamburgerImageButtonOnclick()

      setProfileCardOnclick()

      setFeelingsRecyclerLayoutManager()

      setQuotesRecyclerLayoutManager()

      displayUserInfo(loginResponse)

      getFeelingsData()

      getQuotesData()
    }

  }

  private fun setQuotesRecyclerLayoutManager() {
    binding.quotesRecycler.layoutManager = LinearLayoutManager(
      this.context,
      LinearLayoutManager.VERTICAL,
      false
    )
  }

  private fun setFeelingsRecyclerLayoutManager() {
    binding.feelingsRecycler.layoutManager = LinearLayoutManager(
      this.context,
      LinearLayoutManager.HORIZONTAL,
      false
    )
  }

  private fun setProfileCardOnclick() {
    binding.userImageCard.setOnClickListener {
      parentActivityCallback.setInActivityProfileButtonActive()

      parentFragmentManager.commit {
        setReorderingAllowed(true)
        replace(
          R.id.screenFragmentContainerView,
          ProfileFragment::class.java,
          bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, loginResponse))
        )
      }
    }
  }

  private fun setHamburgerImageButtonOnclick() {
    binding.hamburgerImage.setOnClickListener {
      this.context?.let { it1 -> IntentHelper.startMenuActivity(it1) }
    }
  }

  private fun getQuotesData() {
    Common.retrofitService.getQuotesData()
      .enqueue(object : Callback<QuotesResponse> {
        override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
          val processingQuotesList: List<Quote>? = response.body()?.data
          binding.quotesRecycler.adapter = processingQuotesList?.let { QuotesAdapter(it) }
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

  private fun getFeelingsData() {
    Common.retrofitService.getFeelingsData()
      .enqueue(object : Callback<FeelingsResponse> {
        override fun onResponse(
          call: Call<FeelingsResponse>,
          response: Response<FeelingsResponse>
        ) {
          val processingFeelingsList: List<Feeling>? =
            response.body()?.data?.sortedWith(compareBy { it.position })

          binding.feelingsRecycler.adapter = processingFeelingsList?.let { FeelingsAdapter(it) }
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
  }

  private fun displayUserInfo(processingLoginResponse: LoginResponse?) {
    activity?.let {
      Glide.with(it)
        .load(processingLoginResponse?.avatar)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(binding.userAvatar)
    }


    binding.welcomeHeaderWithUserName.text =
      String.format(getString(R.string.welcomeTextText, processingLoginResponse?.nickName))
  }

  interface ActivityCallback {
    fun setInActivityProfileButtonActive()
  }

}
