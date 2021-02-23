package com.delet_dis.madmeditation.model

import com.google.gson.annotations.SerializedName

data class Feeling(
  @SerializedName("id") val id: Int,
  @SerializedName("title") val title: String,
  @SerializedName("position") val position: Int,
  @SerializedName("image") val image: String
)
