package com.delet_dis.madmeditation.recyclerViewAdapters

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.database.ImageCard
import com.delet_dis.madmeditation.helpers.ConstantsHelper


class GalleryAdapter(private val values: List<ImageCard>, val clickListener: (Int) -> Unit) :
  RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): GalleryHolder {
    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.gallery_recyclerview_item, parent, false)
    return GalleryHolder(itemView)
  }

  override fun onBindViewHolder(holder: GalleryHolder, position: Int) {

    val directory = ContextWrapper(holder.itemView.context).getDir(
      ConstantsHelper.imagesDir,
      Context.MODE_PRIVATE
    ).toString()

    Glide.with(ContextWrapper(holder.itemView.context))
      .load("${directory}/${values[position].imageFilename}")
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(holder.galleryImageView!!)

    holder.galleryImageTime?.text = values[position].time

    holder.itemView.setOnClickListener {
      clickListener(position + 1)
    }
  }

  override fun getItemCount(): Int = values.size

  inner class GalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var galleryImageView: ImageView? = null
    var galleryImageTime: TextView? = null

    init {
      galleryImageView = itemView.findViewById(R.id.cardImage)
      galleryImageTime = itemView.findViewById(R.id.cardTimeText)
    }

  }

}