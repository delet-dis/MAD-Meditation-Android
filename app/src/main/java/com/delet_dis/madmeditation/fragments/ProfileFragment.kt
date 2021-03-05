package com.delet_dis.madmeditation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.delet_dis.madmeditation.database.GalleryViewModel
import com.delet_dis.madmeditation.database.ImageCard
import com.delet_dis.madmeditation.databinding.FragmentProfileScreenBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.FilesHelper
import com.delet_dis.madmeditation.helpers.IntentHelper
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper
import com.delet_dis.madmeditation.model.LoginResponse
import com.delet_dis.madmeditation.recyclerViewAdapters.GalleryAdapter
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {

  private lateinit var hamburgerImage: ImageView
  private lateinit var exitText: TextView

  private lateinit var userAvatar: ImageView
  private lateinit var userNameText: TextView

  private lateinit var galleryRecycler: RecyclerView

  private lateinit var galleryAddCard: CardView

  private lateinit var binding: FragmentProfileScreenBinding

  private var loginResponse: LoginResponse? = null

  private lateinit var galleryViewModel: GalleryViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return if (savedInstanceState == null) {
      binding = FragmentProfileScreenBinding.inflate(layoutInflater)

      loginResponse = requireArguments()
        .getParcelable(ConstantsHelper.loginResponseParcelableName)


      binding.root
    } else {
      view
    }
  }

  private var getContent: ActivityResultLauncher<String>? =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

      if (uri != null) {
        Glide.with(requireContext())
          .asBitmap()
          .load(uri)
          .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
              FilesHelper.saveToInternalStorage(requireContext(), resource, uri.lastPathSegment!!)

              galleryViewModel.insert(
                ImageCard(
                  null,
                  uri.lastPathSegment!!,
                  SimpleDateFormat("HH:mm", Locale.US).format(Calendar.getInstance().time)
                )
              )
            }

            override fun onLoadCleared(placeholder: Drawable?) = Unit
          })
      }
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState == null) {
      galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

      hamburgerImage = binding.hamburgerImage
      exitText = binding.exitText

      userAvatar = binding.userAvatar
      userNameText = binding.userNameText

      galleryRecycler = binding.galleryRecyclerView

      galleryAddCard = binding.galleryCardAddButton

      displayUserInfo(loginResponse)

      hamburgerImage.setOnClickListener {
        this.context?.let { it1 -> IntentHelper.startMenuActivity(it1) }
      }

      exitText.setOnClickListener {
        this.context?.let { it1 -> SharedPreferencesHelper.clearLoginData(it1.applicationContext) }
        this.context?.let { it1 -> IntentHelper.startLoginActivity(it1) }
        activity?.finish()
      }
    }

    galleryRecycler.layoutManager =
      GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

    refreshGalleryRecyclerData()

    galleryAddCard.setOnClickListener {

      if (ContextCompat.checkSelfPermission(
          requireContext(),
          Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        Log.d("test", "its working")
        getContent!!.launch("image/*")
        refreshGalleryRecyclerData()
      } else {
        ActivityCompat.requestPermissions(
          requireActivity(),
          arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
          101
        )
        getContent!!.launch("image/*")
      }

      refreshGalleryRecyclerData()

    }

  }

  private fun refreshGalleryRecyclerData() {
    galleryViewModel.allImages.observe(viewLifecycleOwner, {
      if (it != null) {
        galleryRecycler.adapter = GalleryAdapter(it)
      }
    })
  }

  private fun displayUserInfo(processingLoginResponse: LoginResponse?) {
    activity?.let {
      Glide.with(it)
        .load(processingLoginResponse?.avatar)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(userAvatar)
    }

    userNameText.text =
      processingLoginResponse?.nickName
  }


}