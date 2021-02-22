package com.delet_dis.madmeditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.model.LoginResponse

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    var loginResponse = intent.extras?.getParcelable<LoginResponse>(ConstantsHelper.loginResponseParcelableName)

    if(loginResponse!==null){
      Toast.makeText(applicationContext, loginResponse.email, Toast.LENGTH_SHORT).show()
    }
  }
}