package com.delet_dis.madmeditation.helpers

import android.app.Activity
import android.view.WindowManager

object WindowHelper {
  fun setWindowNoLimits(activity: Activity) {
    activity.window.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
  }
}