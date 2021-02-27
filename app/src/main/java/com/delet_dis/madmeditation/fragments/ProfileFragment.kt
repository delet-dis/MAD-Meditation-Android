package com.delet_dis.madmeditation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.delet_dis.madmeditation.databinding.FragmentProfileScreenBinding

class ProfileFragment : Fragment() {

  private lateinit var hamburgerImage: ImageView

  private lateinit var binding: FragmentProfileScreenBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    return if (savedInstanceState == null) {
      binding = FragmentProfileScreenBinding.inflate(layoutInflater)

      binding.root
    } else {
      view
    }

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if(savedInstanceState == null){
      hamburgerImage = binding.hamgurgerImage
    }
  }
}