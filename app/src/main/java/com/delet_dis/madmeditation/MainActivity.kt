package com.delet_dis.madmeditation

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.delet_dis.madmeditation.databinding.ActivityMainBinding
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


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = createViewBinding()

    setContentView(view)

    findViewElements()

    val loginResponse =
      getParceledLoginResponse()

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
  }

  private fun findViewElements() {
    fragmentContainerView = binding.screenFragmentContainerView

    footerMainImageView = binding.footerLogoImageViewAsButton
    footerListenImageView = binding.footerListenImageViewAsButton
    footerUserImageView = binding.footerUserImageViewAsButton
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