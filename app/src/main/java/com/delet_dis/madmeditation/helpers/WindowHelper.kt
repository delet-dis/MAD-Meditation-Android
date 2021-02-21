package com.delet_dis.madmeditation.helpers

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import com.delet_dis.madmeditation.R

object WindowHelper {
  fun setWindowNoLimits(activity: Activity) {
    activity.window.setFlags(
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
  }
}