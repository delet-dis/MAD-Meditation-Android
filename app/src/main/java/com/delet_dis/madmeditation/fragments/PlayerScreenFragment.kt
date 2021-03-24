package com.delet_dis.madmeditation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delet_dis.madmeditation.databinding.FragmentPlayerScreenBinding

class PlayerScreenFragment : Fragment() {

  private lateinit var binding: FragmentPlayerScreenBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return if (savedInstanceState == null) {
      binding = FragmentPlayerScreenBinding.inflate(layoutInflater)

      binding.root
    } else {
      view
    }
  }
}