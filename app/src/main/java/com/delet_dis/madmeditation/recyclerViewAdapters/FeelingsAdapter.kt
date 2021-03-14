package com.delet_dis.madmeditation.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.databinding.FeelingsRecyclerviewItemBinding
import com.delet_dis.madmeditation.model.Feeling

class FeelingsAdapter(private val values: List<Feeling>) :
  RecyclerView.Adapter<FeelingsAdapter.FeelingsHolder>() {
  private lateinit var binding: FeelingsRecyclerviewItemBinding

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): FeelingsHolder {

    binding =
      FeelingsRecyclerviewItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )

    return FeelingsHolder(binding)
  }

  override fun onBindViewHolder(holder: FeelingsHolder, position: Int) {
    with(values[position]) {
      binding.feelingName.text = title

      binding.feelingImage.let {
        Glide.with(holder.itemView)
          .load(image)
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(it)
      }
    }
  }

  override fun getItemCount(): Int = values.size

  inner class FeelingsHolder(binding: FeelingsRecyclerviewItemBinding) :
    RecyclerView.ViewHolder(binding.root)
}