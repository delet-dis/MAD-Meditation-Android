package com.delet_dis.madmeditation.helpers

import android.content.Context
import android.content.Intent
import com.delet_dis.madmeditation.MenuActivity

object IntentHelper {

  fun startMenuActivity(context: Context) {
    val intent = Intent(context, MenuActivity::class.java)
    context.startActivity(intent)
  }

}