package com.delet_dis.madmeditation

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.delet_dis.madmeditation.databinding.ActivityMainBinding
import com.delet_dis.madmeditation.fragments.MainScreenFragment
import com.delet_dis.madmeditation.fragments.PlayerScreenFragment
import com.delet_dis.madmeditation.fragments.ProfileFragment
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.MainFragmentFooterButtonsHelper
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper
import com.delet_dis.madmeditation.helpers.ToastHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MainScreenFragment.ToMainActivityCallback {
  private lateinit var binding: ActivityMainBinding

  private lateinit var fragmentContainerView: FragmentContainerView

  private lateinit var footerMainImageButton: ImageView
  private lateinit var footerPlayerImageButton: ImageView
  private lateinit var footerProfileImageButton: ImageView

  private var processingLoginResponse: LoginResponse? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = createViewBinding()

    setContentView(view)

    findViewElements()

    val loginResponse =
      getParceledLoginResponse()

    if (loginResponse == null) {
      val loginRequest = SharedPreferencesHelper.getLoginData(applicationContext)

      postLoginData(loginRequest)
    } else {
      processingLoginResponse = loginResponse

      createMainFragment(processingLoginResponse)
    }

    footerPlayerImageButton.setOnClickListener {
      manageFooterButtons(MainFragmentFooterButtonsHelper.PLAYER_SCREEN)
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<PlayerScreenFragment>(R.id.screenFragmentContainerView)
      }
    }

    manageFooterButtons(MainFragmentFooterButtonsHelper.MAIN_SCREEN)

    footerMainImageButton.setOnClickListener {
      manageFooterButtons(MainFragmentFooterButtonsHelper.MAIN_SCREEN)
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(
          R.id.screenFragmentContainerView,
          MainScreenFragment::class.java,
          bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, processingLoginResponse))
        )
      }
    }

    footerProfileImageButton.setOnClickListener {
      manageFooterButtons(MainFragmentFooterButtonsHelper.PROFILE_SCREEN)
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(
          R.id.screenFragmentContainerView,
          ProfileFragment::class.java,
          bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, processingLoginResponse))
        )

      }
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

  private fun findViewElements() {
    fragmentContainerView = binding.screenFragmentContainerView

    footerMainImageButton = binding.footerLogoImageButton
    footerPlayerImageButton = binding.footerPlayerImageButton
    footerProfileImageButton = binding.footerProfileImageButton
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
        bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, processingLoginResponse)),
        ConstantsHelper.mainFragmentTag
      )
    }
  }

  private fun manageFooterButtons(activeButton: MainFragmentFooterButtonsHelper) {
    when (activeButton) {
      MainFragmentFooterButtonsHelper.MAIN_SCREEN -> {
        footerProfileImageButton.setImageResource(R.drawable.footer_profile_non_active)
        footerPlayerImageButton.setImageResource(R.drawable.footer_player_non_active)

        footerMainImageButton.setImageResource(R.drawable.footer_menu_active)

        setPlayerButtonNonActive()

        setProfileButtonNonActive()

        footerMainImageButton.scaleX = ConstantsHelper.scaleMainCoefficientActive
        footerMainImageButton.scaleY = ConstantsHelper.scaleMainCoefficientActive
      }

      MainFragmentFooterButtonsHelper.PLAYER_SCREEN -> {
        footerProfileImageButton.setImageResource(R.drawable.footer_profile_non_active)
        footerMainImageButton.setImageResource(R.drawable.footer_menu_non_active)

        footerPlayerImageButton.setImageResource(R.drawable.footer_player_active)

        setLogoButtonNonActive()

        setProfileButtonNonActive()

        footerPlayerImageButton.scaleX = ConstantsHelper.scaleCoefficientActive
        footerPlayerImageButton.scaleY = ConstantsHelper.scaleCoefficientActive
      }

      MainFragmentFooterButtonsHelper.PROFILE_SCREEN -> {
        footerPlayerImageButton.setImageResource(R.drawable.footer_player_non_active)
        footerMainImageButton.setImageResource(R.drawable.footer_menu_non_active)

        footerProfileImageButton.setImageResource(R.drawable.footer_profile_active)

        setLogoButtonNonActive()

        setPlayerButtonNonActive()

        footerProfileImageButton.scaleX = ConstantsHelper.scaleCoefficientActive
        footerProfileImageButton.scaleY = ConstantsHelper.scaleCoefficientActive
      }
    }
  }

  private fun setProfileButtonNonActive() {
    footerProfileImageButton.scaleX = ConstantsHelper.scaleCoefficientNonActive
    footerProfileImageButton.scaleY = ConstantsHelper.scaleCoefficientNonActive
  }

  private fun setPlayerButtonNonActive() {
    footerPlayerImageButton.scaleX = ConstantsHelper.scaleCoefficientNonActive
    footerPlayerImageButton.scaleY = ConstantsHelper.scaleCoefficientNonActive
  }

  private fun setLogoButtonNonActive() {
    footerMainImageButton.scaleX = ConstantsHelper.scaleMainCoefficientNonActive
    footerMainImageButton.scaleY = ConstantsHelper.scaleMainCoefficientNonActive
  }

  override fun setProfileButtonActive() {
    manageFooterButtons(MainFragmentFooterButtonsHelper.PROFILE_SCREEN)
  }

}