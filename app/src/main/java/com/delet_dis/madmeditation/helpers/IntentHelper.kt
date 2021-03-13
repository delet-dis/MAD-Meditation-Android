package com.delet_dis.madmeditation.helpers

import android.content.Context
import android.content.Intent
import com.delet_dis.madmeditation.GalleryImageViewActivity
import com.delet_dis.madmeditation.LoginActivity
import com.delet_dis.madmeditation.MenuActivity

object IntentHelper {

  fun startMenuActivity(context: Context) {
    val intent = Intent(context, MenuActivity::class.java)
    context.startActivity(intent)
  }

  fun startLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
  }

  fun startGalleryActivity(context: Context, imageId: Int) {
    val intent = Intent(context, GalleryImageViewActivity::class.java)
    intent.putExtra(ConstantsHelper.idOfParceledImage, imageId)
    context.startActivity(intent)
  }

}