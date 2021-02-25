package com.delet_dis.madmeditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.databinding.FragmentMainScreenBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.*
import com.delet_dis.madmeditation.recyclerView.FeelingsAdapter
import com.delet_dis.madmeditation.recyclerView.QuotesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainScreenFragment : Fragment() {

  private lateinit var userAvatar: ImageView
  private lateinit var welcomeTextWithUserName: TextView

  private lateinit var feelingsRecycler: RecyclerView

  private lateinit var quotesRecycler: RecyclerView

  private lateinit var binding: FragmentMainScreenBinding

  private lateinit var loginResponse: LoginResponse

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMainScreenBinding.inflate(layoutInflater)

    loginResponse = requireArguments()
      .getParcelable(ConstantsHelper.loginResponseParcelableName)!!

    return binding.root

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    userAvatar = binding.userAvatar
    welcomeTextWithUserName = binding.welcomeHeaderWithUserName

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
          Toast.makeText(
            activity,
            getString(R.string.networkErrorMessage),
            Toast.LENGTH_SHORT
          ).show()
        }

      })

    Common.retrofitService.getQuotesData()
      .enqueue(object : Callback<QuotesResponse> {
        override fun onResponse(call: Call<QuotesResponse>, response: Response<QuotesResponse>) {
          val processingQuotesList: List<Quote> = response.body()!!.data
          quotesRecycler.adapter = QuotesAdapter(processingQuotesList)
        }

        override fun onFailure(call: Call<QuotesResponse>, t: Throwable) {
          Toast.makeText(
            activity,
            getString(R.string.networkErrorMessage),
            Toast.LENGTH_SHORT
          ).show()
        }

      })
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