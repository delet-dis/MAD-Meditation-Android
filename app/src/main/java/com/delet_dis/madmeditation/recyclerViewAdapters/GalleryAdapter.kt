package com.delet_dis.madmeditation.recyclerViewAdapters

import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.database.ImageCard
import com.delet_dis.madmeditation.helpers.ConstantsHelper
import com.delet_dis.madmeditation.helpers.ToastHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class GalleryAdapter(private val values: List<ImageCard>) :
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

    Glide.with(ContextWrapper(holder.itemView.context))
      .asBitmap()
      .load(values[position].imagePath)
      .into(holder.galleryImageView!!)

    holder.galleryImageTime?.text = values[position].time
  }

  override fun getItemCount(): Int = values.size

  class GalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var galleryImageView: ImageView? = null
    var galleryImageTime: TextView? = null

    init {
      galleryImageView = itemView.findViewById(R.id.cardImage)

      galleryImageTime = itemView.findViewById(R.id.cardTimeText)
    }

  }

}