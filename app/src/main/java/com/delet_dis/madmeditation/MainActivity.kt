package com.delet_dis.madmeditation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.delet_dis.madmeditation.databinding.ActivityMainBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.SharedPrefsHelper
import com.delet_dis.madmeditation.http.common.Common
import com.delet_dis.madmeditation.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

  private lateinit var fragmentContainerView: FragmentContainerView


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = createViewBinding()

    setContentView(view)

    fragmentContainerView = binding.screenFragmentContainerView

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
            Toast.makeText(
              applicationContext,
              getString(R.string.networkErrorMessage),
              Toast.LENGTH_SHORT
            ).show()
          }

        })
    } else {
      processingLoginResponse = loginResponse

      createMainFragment(processingLoginResponse)
    }
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