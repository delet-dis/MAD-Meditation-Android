package com.delet_dis.madmeditation.helpers

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object FilesHelper {

  fun saveToInternalStorage(
    context: Context,
    bitmapImage: Bitmap,
    filename: String
  ): String? {
    val directory: File = context.getDir(ConstantsHelper.imagesDir, Context.MODE_PRIVATE)

    val imagePath = File(directory, filename)
    var fileOutputStream: FileOutputStream? = null
    try {
      fileOutputStream = FileOutputStream(imagePath)

      bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      try {
        fileOutputStream?.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
    return directory.absolutePath
  }


}


