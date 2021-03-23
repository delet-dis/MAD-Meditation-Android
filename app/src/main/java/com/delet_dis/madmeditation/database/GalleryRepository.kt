package com.delet_dis.madmeditation.database

import android.content.Context
import kotlinx.coroutines.flow.first

class GalleryRepository(val context: Context) {
  suspend fun getImageById(id: Int): ImageCard {
    return GalleryDatabase.getAppDataBase(context).galleryDao().getById(id.toLong()).first()
  }

  suspend fun clearTables(afterCallFunction: () -> Unit) =
    run {
      GalleryDatabase.getAppDataBase(context).galleryDao().nukeTable()

      afterCallFunction()
    }

  suspend fun removeImageById(id: Int, afterCallFunction: () -> Unit) =
    run {
      GalleryDatabase.getAppDataBase(context).galleryDao().removeById(id.toLong())

      afterCallFunction()
    }

  suspend fun insert(imageCard: ImageCard) {
    GalleryDatabase.getAppDataBase(context).galleryDao().insert(imageCard)
  }
}