package com.delet_dis.madmeditation.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

  private val galleryDao: GalleryDAO = GalleryDatabase.getAppDataBase(application)!!.galleryDao()

  val allImages: LiveData<List<ImageCard>> =
    GalleryDatabase.getAppDataBase(application)!!.galleryDao().getAll()

  fun insert(galleryImageCard: ImageCard) = viewModelScope.launch {
    galleryDao.insert(galleryImageCard)
  }

}