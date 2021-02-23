package com.delet_dis.madmeditation.model

import com.google.gson.annotations.SerializedName

data class FeelingsResponse(
  @SerializedName("success") val success: Boolean,
  @SerializedName("data") val data: List<Feeling>
)
