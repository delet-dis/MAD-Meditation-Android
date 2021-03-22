package com.delet_dis.madmeditation.helpers

import android.app.Activity
import android.content.DialogInterface
import com.delet_dis.madmeditation.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


object AlertDialogHelper {
  fun buildAlertDialog(activity: Activity, stringResourceId: Int) {
    MaterialAlertDialogBuilder(activity)
      .setTitle(activity.applicationContext.getString(R.string.alertDialogLoginFailedTitle))
      .setMessage(activity.applicationContext.getString(stringResourceId))
      .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
      .show()
  }
}