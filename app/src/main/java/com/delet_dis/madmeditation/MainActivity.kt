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
import com.delet_dis.madmeditation.helpers.SharedPrefsHelper
import com.delet_dis.madmeditation.helpers.ToastHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

  private lateinit var fragmentContainerView: FragmentContainerView

  private lateinit var footerMainImageView: ImageView
  private lateinit var footerListenImageView: ImageView
  private lateinit var footerUserImageView: ImageView

  private var processingLoginResponse: LoginResponse? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = createViewBinding()

    setContentView(view)

    findViewElements()

    val loginResponse =
      getParceledLoginResponse()

    if (loginResponse == null) {
      val loginRequest = SharedPrefsHelper.getLoginData(applicationContext)

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
    } else {
      processingLoginResponse = loginResponse

      createMainFragment(processingLoginResponse)
    }

    footerListenImageView.setOnClickListener {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<PlayerScreenFragment>(R.id.screenFragmentContainerView)
      }
    }

    footerMainImageView.setOnClickListener {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace(
          R.id.screenFragmentContainerView,
          MainScreenFragment::class.java,
          bundleOf(Pair(ConstantsHelper.loginResponseParcelableName, processingLoginResponse))
        )
      }
    }

    footerUserImageView.setOnClickListener {
      supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<ProfileFragment>(R.id.screenFragmentContainerView)
      }
    }
  }

  private fun findViewElements() {
    fragmentContainerView = binding.screenFragmentContainerView

    footerMainImageView = binding.footerLogoImageViewAsButton
    footerListenImageView = binding.footerListenImageViewAsButton
    footerUserImageView = binding.footerProfileImageViewAsButton
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

}