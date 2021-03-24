package com.delet_dis.madmeditation.recyclerViewAdapters

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.database.ImageCard
import com.delet_dis.madmeditation.databinding.GalleryRecyclerviewItemBinding
import com.delet_dis.madmeditation.repositories.ConstantsRepository


class GalleryAdapter(private val values: List<ImageCard>, val clickListener: (Int) -> Unit) :
  RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {
  private lateinit var binding: GalleryRecyclerviewItemBinding

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): GalleryHolder {

    binding = GalleryRecyclerviewItemBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )

    return GalleryHolder(binding)
  }

  override fun onBindViewHolder(holder: GalleryHolder, position: Int) {

    with(values[position]) {
      ContextWrapper(holder.itemView.context).getDir(
        ConstantsRepository.imagesDir,
        Context.MODE_PRIVATE
      ).toString()

      Glide.with(ContextWrapper(holder.itemView.context))
        .load(imageFilename)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(binding.cardImage)

      binding.cardTimeText.text = time

      holder.itemView.setOnClickListener {
        id?.let { it1 -> clickListener(it1.toInt()) }
      }
    }
  }

  override fun getItemCount(): Int = values.size

  inner class GalleryHolder(binding: GalleryRecyclerviewItemBinding) :
    RecyclerView.ViewHolder(binding.root)
}