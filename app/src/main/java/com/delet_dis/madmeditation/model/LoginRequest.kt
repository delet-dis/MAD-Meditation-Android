package com.delet_dis.madmeditation.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
  val email: String,
  val password: String
)