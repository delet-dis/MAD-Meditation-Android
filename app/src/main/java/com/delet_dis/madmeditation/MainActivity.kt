package com.delet_dis.madmeditation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.delet_dis.madmeditation.databinding.ActivityMainBinding
import com.delet_dis.madmeditation.fragments.MainScreenFragment
import com.delet_dis.madmeditation.fragments.PlayerScreenFragment
import com.delet_dis.madmeditation.fragments.ProfileFragment
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper
import com.delet_dis.madmeditation.helpers.ToastHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MainScreenFragment.ActivityCallback {
  private lateinit var binding: ActivityMainBinding

  private var processingLoginResponse: LoginResponse? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(createViewBinding())

    val loginResponse = getParceledLoginResponse()

    if (loginResponse == null) {
      val loginRequest = SharedPreferencesHelper.getLoginData(applicationContext)
      postLoginData(loginRequest)
    } else {
      processingLoginResponse = loginResponse
      createMainFragment(processingLoginResponse)
    }

    setBottomNavigationViewOnclick()
  }

  private fun setBottomNavigationViewOnclick() {
    binding.bottomNavigationView.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.mainFragmentButton -> {
          footerMainButtonOnclick()
          return@setOnNavigationItemSelectedListener true
        }

        R.id.playerFragmentButton -> {
          footerPlayerButtonOnclick()
          return@setOnNavigationItemSelectedListener true
        }

        R.id.profileFragmentButton -> {
          footerProfileButtonOnclick()
          return@setOnNavigationItemSelectedListener true
        }

        else -> {
          return@setOnNavigationItemSelectedListener false
        }
      }
    }
  }

  private fun footerProfileButtonOnclick() {
    supportFragmentManager.commit {
      setReorderingAllowed(true)
      replace(
        R.id.screenFragmentContainerView,
        ProfileFragment::class.java,
        bundleOf(ConstantsHelper.loginResponseParcelableName to processingLoginResponse)
      )

    }
  }

  private fun footerMainButtonOnclick() {
    supportFragmentManager.commit {
      setReorderingAllowed(true)
      replace(
        R.id.screenFragmentContainerView,
        MainScreenFragment::class.java,
        bundleOf(ConstantsHelper.loginResponseParcelableName to processingLoginResponse)
      )
    }
  }

  private fun footerPlayerButtonOnclick() {
    supportFragmentManager.commit {
      setReorderingAllowed(true)
      replace<PlayerScreenFragment>(R.id.screenFragmentContainerView)
    }
  }

  private fun postLoginData(loginRequest: LoginRequest) {
    Common.retrofitService.postLoginData(loginRequest)
      .enqueue(object : Callback<LoginResponse> {

        override fun onResponse(
          call: Call<LoginResponse>,
          response: Response<LoginResponse>
        ) {
          processingLoginResponse = response.body()

          createMainFragment(processingLoginResponse)
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
          ToastHelper.createErrorToast(applicationContext, R.string.networkErrorMessage)
        }

      })
  }

  private fun getParceledLoginResponse() =
    intent.extras?.getParcelable<LoginResponse>(ConstantsHelper.loginResponseParcelableName)

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityMainBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun createMainFragment(processingLoginResponse: LoginResponse?) {
    supportFragmentManager.commit {
      setReorderingAllowed(true)
      add(
        R.id.screenFragmentContainerView,
        MainScreenFragment::class.java,
        bundleOf(ConstantsHelper.loginResponseParcelableName to processingLoginResponse),
        ConstantsHelper.mainFragmentTag
      )
    }
  }

  override fun setInActivityProfileButtonActive() {
    binding.bottomNavigationView.selectedItemId = R.id.profileFragmentButton
  }
}