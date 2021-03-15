package com.delet_dis.madmeditation

import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.database.GalleryDatabase
import com.delet_dis.madmeditation.databinding.ActivityGalleryImageViewBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryImageViewActivity : AppCompatActivity() {
  private lateinit var binding: ActivityGalleryImageViewBinding

  private var imageId: Int? = null

  private var viewModelJob = Job()
  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(createViewBinding())

    imageId = intent?.extras?.getInt(ConstantsHelper.idOfParceledImage)

    loadImageFromDatabase()

    registerDeleteButtonOnclick()

    registerCloseButtonOnclick()
  }

  private fun registerCloseButtonOnclick() {
    binding.closeButton.setOnClickListener {
      finishActivity()
    }
  }

  private fun finishActivity() {
    finish()
  }

  private fun registerDeleteButtonOnclick() {
    binding.deleteButton.setOnClickListener {

      lifecycleScope.launch {
        imageId?.let { it1 ->
          GalleryDatabase.getAppDataBase(application).galleryDao().removeImageById(
            application,
            it1
          ) { finishActivity() }
        }
      }
    }
  }

  private fun loadImageFromDatabase() {
    uiScope.launch {
      val imageCard = imageId?.let {
        GalleryDatabase.getAppDataBase(application).galleryDao()
          .getImageById(application, it)
      }

      val directory = ContextWrapper(applicationContext).getDir(
        ConstantsHelper.imagesDir,
        MODE_PRIVATE
      ).toString()


      if (imageCard != null) {
        Glide.with(ContextWrapper(applicationContext))
          .load(imageCard.imageFilename)
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(binding.galleryImage)
      }
    }
  }

  private fun createViewBinding(): ConstraintLayout {
    binding = ActivityGalleryImageViewBinding.inflate(layoutInflater)
    return binding.root
  }
}