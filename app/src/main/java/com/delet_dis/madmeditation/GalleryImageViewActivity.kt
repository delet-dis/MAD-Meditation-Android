package com.delet_dis.madmeditation

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.database.GalleryViewModel
import com.delet_dis.madmeditation.databinding.ActivityGalleryImageViewBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.jsibbold.zoomage.ZoomageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryImageViewActivity : AppCompatActivity() {
  private lateinit var binding: ActivityGalleryImageViewBinding

  private lateinit var image: ZoomageView

  private lateinit var deleteButton: Button
  private lateinit var closeButton: Button

  private var imageId: Int? = null

  private var viewModelJob = Job()
  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGalleryImageViewBinding.inflate(layoutInflater)
    setContentView(binding.root)

    imageId = intent?.extras?.getInt(ConstantsHelper.idOfParceledImage)

    image = binding.galleryImage

    deleteButton = binding.deleteButton
    closeButton = binding.closeButton

    uiScope.launch {
      val imageCard = GalleryViewModel(application).getImageById(application, imageId!!)

      val directory = ContextWrapper(applicationContext).getDir(
        ConstantsHelper.imagesDir,
        Context.MODE_PRIVATE
      ).toString()


      Glide.with(ContextWrapper(applicationContext))
        .load("${directory}/${imageCard.imageFilename}")
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(image)
    }
  }
}