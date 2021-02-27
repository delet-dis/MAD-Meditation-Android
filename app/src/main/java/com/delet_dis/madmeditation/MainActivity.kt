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
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper
import com.delet_dis.madmeditation.helpers.ToastHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginRequest
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

  private lateinit var fragmentContainerView: FragmentContainerView

  private lateinit var footerMainImageView: ImageView
  private lateinit var footerPlayerImageView: ImageView
  private lateinit var footerProfileImageView: ImageView

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

    footerPlayerImageView.setOnClickListener {
      setPlayerButtonActive()
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<PlayerScreenFragment>(R.id.screenFragmentContainerView)
      }
    }

    setLogoButtonActive()

    footerMainImageView.setOnClickListener {
      setLogoButtonActive()
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(
          R.id.screenFragmentContainerView,
          MainScreenFragment::class.java,
          bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, processingLoginResponse))
        )
      }
    }

    footerProfileImageView.setOnClickListener {
      setProfileButtonActive()
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<ProfileFragment>(R.id.screenFragmentContainerView)
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

    footerMainImageView = binding.footerLogoImageViewAsButton
    footerPlayerImageView = binding.footerPlayerImageViewAsButton
    footerProfileImageView = binding.footerProfileImageViewAsButton
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
        bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, processingLoginResponse))
      )
    }
  }

  private fun setProfileButtonActive() {
    footerPlayerImageView.setImageResource(R.drawable.footer_player_non_active)
    footerMainImageView.setImageResource(R.drawable.footer_menu_non_active)

    footerProfileImageView.setImageResource(R.drawable.footer_profile_active)

    setLogoButtonNonActive()

    setPlayerButtonNonActive()

    footerProfileImageView.scaleX = ConstantsHelper.scaleCoefficientActive
    footerProfileImageView.scaleY = ConstantsHelper.scaleCoefficientActive
  }

  private fun setProfileButtonNonActive() {
    footerProfileImageView.scaleX = ConstantsHelper.scaleCoefficientNonActive
    footerProfileImageView.scaleY = ConstantsHelper.scaleCoefficientNonActive
  }

  private fun setPlayerButtonActive() {
    footerProfileImageView.setImageResource(R.drawable.footer_profile_non_active)
    footerMainImageView.setImageResource(R.drawable.footer_menu_non_active)

    footerPlayerImageView.setImageResource(R.drawable.footer_player_active)

    setLogoButtonNonActive()

    setProfileButtonNonActive()

    footerPlayerImageView.scaleX = ConstantsHelper.scaleCoefficientActive
    footerPlayerImageView.scaleY = ConstantsHelper.scaleCoefficientActive
  }

  private fun setPlayerButtonNonActive() {
    footerPlayerImageView.scaleX = ConstantsHelper.scaleCoefficientNonActive
    footerPlayerImageView.scaleY = ConstantsHelper.scaleCoefficientNonActive
  }

  private fun setLogoButtonActive() {
    footerProfileImageView.setImageResource(R.drawable.footer_profile_non_active)
    footerPlayerImageView.setImageResource(R.drawable.footer_player_non_active)

    footerMainImageView.setImageResource(R.drawable.footer_menu_active)

    setPlayerButtonNonActive()

    setProfileButtonNonActive()

    footerMainImageView.scaleX = ConstantsHelper.scaleMainCoefficientActive
    footerMainImageView.scaleY = ConstantsHelper.scaleMainCoefficientActive
  }

  private fun setLogoButtonNonActive() {
    footerMainImageView.scaleX = ConstantsHelper.scaleMainCoefficientNonActive
    footerMainImageView.scaleY = ConstantsHelper.scaleMainCoefficientNonActive
  }

}