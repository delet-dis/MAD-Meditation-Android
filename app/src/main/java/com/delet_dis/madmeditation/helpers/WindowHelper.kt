package com.delet_dis.madmeditation.helpers

import android.app.Activity
import android.view.View
import com.delet_dis.madmeditation.R

object WindowHelper {
  fun setWindowTransparent(activity: Activity) {
    val window = activity.window

    val halfTranslucentColor = activity.getColor(R.color.halfTranslucent)

    window.statusBarColor = halfTranslucentColor
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
  }
}