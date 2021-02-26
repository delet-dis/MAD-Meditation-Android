package com.delet_dis.madmeditation.helpers

import android.content.Context
import android.widget.Toast
import com.delet_dis.madmeditation.R

object ToastHelper {
  fun createErrorToast(context: Context, stringResourceId: Int) {
    Toast.makeText(
      context,
      context.getString(stringResourceId),
      Toast.LENGTH_SHORT
    ).show()
  }
}