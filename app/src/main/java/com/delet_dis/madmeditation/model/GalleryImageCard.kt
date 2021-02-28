package com.delet_dis.madmeditation.model

import android.graphics.Bitmap

data class GalleryImageCard(
  var backgroundColorResource: Int? = null,
  var isFillingParent: Boolean?,
  var image: Bitmap,
  var time: String?
)
