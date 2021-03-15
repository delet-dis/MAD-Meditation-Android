package com.delet_dis.madmeditation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
  val id: String?,
  val email: String?,
  val nickName: String?,
  val avatar: String?,
  val token: String?,
  val error: String?,
  val success: Boolean?
) : Parcelable