package com.delet_dis.madmeditation.fragments

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.database.GalleryDatabase
import com.delet_dis.madmeditation.database.ImageCard
import com.delet_dis.madmeditation.databinding.FragmentProfileScreenBinding
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.IntentHelper
import com.delet_dis.madmeditation.helpers.SharedPreferencesHelper
import com.delet_dis.madmeditation.model.LoginResponse
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

    activity?.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
      GalleryDatabase.getAppDataBase(requireContext())?.galleryDao()?.insert(
        ImageCard(
          null,

          requireContext().contentResolver.openInputStream(uri!!)?.buffered()
            ?.use { it.readBytes() }!!,

          Calendar.getInstance().time.toString()
        )
      )
    }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState == null) {
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

    galleryRecycler.layoutManager = GridLayoutManager(this.context, 2)


    galleryAddCard.setOnClickListener {

      ActivityCompat.requestPermissions(
        requireActivity(),
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        101
      )


      Log.d("test", "calling image picking")
      getContent?.launch("image/*")
    }

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