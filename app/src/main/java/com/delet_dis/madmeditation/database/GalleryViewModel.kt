package com.delet_dis.madmeditation.database

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

  private val galleryDao: GalleryDAO = GalleryDatabase.getAppDataBase(application)!!.galleryDao()

  val allImages: LiveData<List<ImageCard>> =
    GalleryDatabase.getAppDataBase(application)!!.galleryDao().getAll()

  suspend fun getImageById(application: Application, id: Int): ImageCard {
    return GalleryDatabase.getAppDataBase(application)!!.galleryDao().getById(id.toLong()).first()
  }

  fun clearTables(application: Application, afterCallFunction: () -> Unit) = viewModelScope.launch {
    GalleryDatabase.getAppDataBase(application)!!.galleryDao().nukeTable()

    val sqLiteDatabase: SQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
      application.applicationContext.getDatabasePath("galleryDB"),
      null
    )

    val deleteSQLiteSequence = "DELETE FROM sqlite_sequence WHERE name = 'ImageCard'"

    sqLiteDatabase.execSQL(deleteSQLiteSequence)

    afterCallFunction()
  }

  fun removeImageById(application: Application, id: Int, afterCallFunction: () -> Unit) =
    viewModelScope.launch {
      GalleryDatabase.getAppDataBase(application)!!.galleryDao().removeById(id.toLong())

      afterCallFunction()
    }

  fun insert(galleryImageCard: ImageCard) = viewModelScope.launch {
    galleryDao.insert(galleryImageCard)
  }
}