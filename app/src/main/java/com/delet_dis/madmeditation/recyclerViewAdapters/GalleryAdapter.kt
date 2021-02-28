package com.delet_dis.madmeditation.recyclerViewAdapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.database.ImageCard

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
    holder.galleryImageView?.setImageBitmap(
      BitmapFactory.decodeByteArray(
        values[position].image,
        0,
        values[position].image.size
      )
    )
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