package com.delet_dis.madmeditation.fragments

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.database.GalleryDatabase
import com.delet_dis.madmeditation.repositories.GalleryDatabaseRepository
import com.delet_dis.madmeditation.database.ImageCard
import com.delet_dis.madmeditation.databinding.FragmentProfileScreenBinding
import com.delet_dis.madmeditation.helpers.*
import com.delet_dis.madmeditation.model.LoginResponse
import com.delet_dis.madmeditation.recyclerViewAdapters.GalleryAdapter
import com.delet_dis.madmeditation.repositories.ConstantsRepository
import com.delet_dis.madmeditation.repositories.SharedPreferencesRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class ProfileScreenFragment : Fragment() {

  private lateinit var binding: FragmentProfileScreenBinding

  private var loginResponse: LoginResponse? = null

  private val requestPermissionLauncher = registerForActivityResult(
    RequestPermission()
  ) { isGranted: Boolean ->
    if (!isGranted) {
      ToastHelper.createErrorToast(requireContext(), R.string.noInternalStorageAccess)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return if (savedInstanceState == null) {
      binding = FragmentProfileScreenBinding.inflate(layoutInflater)

      loginResponse = requireArguments()
        .getParcelable(ConstantsRepository.loginResponseParcelableName)

      binding.root
    } else {
      view
    }
  }

  private var getContent: ActivityResultLauncher<String>? =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

      val directory = ContextWrapper(requireContext()).getDir(
        ConstantsRepository.imagesDir,
        Context.MODE_PRIVATE
      ).toString()

      if (uri != null) {
        Glide.with(requireContext())
          .asBitmap()
          .load(uri)
          .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
              uri.lastPathSegment?.let {
                FilesHelper.saveToInternalStorage(
                  requireContext(),
                  resource,
                  it
                )
              }

              lifecycleScope.launch {
                GalleryDatabaseRepository(requireContext()).insert(
                  ImageCard(
                    null,
                    "${directory}/${uri.lastPathSegment}",
                    SimpleDateFormat("HH:mm", Locale.US).format(Calendar.getInstance().time)
                  )
                )
              }
            }

            override fun onLoadCleared(placeholder: Drawable?) = Unit
          })
      }
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState == null) {

      setGalleryRecyclerLayoutManager()

      displayUserInfo(loginResponse)

      setHamburgerImageButtonOnclick()

      setExitButtonOnclick()

      setGalleryAddCardOnclick()
    }

    refreshGalleryRecyclerData()

  }

  private fun setGalleryRecyclerLayoutManager() {
    binding.galleryRecyclerView.layoutManager =
      GridLayoutManager(
        requireContext(),
        2,
        GridLayoutManager.VERTICAL,
        false
      )
  }

  private fun setGalleryAddCardOnclick() {
    binding.galleryCardButton.setOnClickListener {

      if (ContextCompat.checkSelfPermission(
          requireContext(),
          Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        getContent?.launch("image/*")
        refreshGalleryRecyclerData()
      } else {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        getContent?.launch("image/*")
      }

      refreshGalleryRecyclerData()

    }
  }

  private fun clearGalleryImagesAndStartLoginActivity() {
    val directory = ContextWrapper(requireContext()).getDir(
      ConstantsRepository.imagesDir,
      Context.MODE_PRIVATE
    )

    directory.deleteRecursively()

    IntentHelper.startLoginActivity(requireContext())

    activity?.finish()
  }

  private fun setExitButtonOnclick() {
    binding.exitText.setOnClickListener {
      SharedPreferencesRepository(requireContext()).clearLoginData()


      lifecycleScope.launch {
        GalleryDatabaseRepository(requireContext())
          .clearTables { clearGalleryImagesAndStartLoginActivity() }
      }
    }
  }

  private fun setHamburgerImageButtonOnclick() {
    binding.hamburgerImage.setOnClickListener {
      this.context?.let { it1 -> IntentHelper.startMenuActivity(it1) }
    }
  }

  private fun refreshGalleryRecyclerData() {

    GalleryDatabase.getAppDataBase(requireContext()).galleryDao().getAll()
      .observe(viewLifecycleOwner, { list ->
        if (list != null) {
          binding.galleryRecyclerView.adapter = GalleryAdapter(list) {
            IntentHelper.startGalleryActivity(requireContext(), it)
          }
        }
      })
  }


  private fun displayUserInfo(processingLoginResponse: LoginResponse?) {
    activity?.let {
      Glide.with(it)
        .load(processingLoginResponse?.avatar)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(binding.userAvatar)
    }

    binding.userNameText.text =
      processingLoginResponse?.nickName
  }
}