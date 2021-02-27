package com.delet_dis.madmeditation.recyclerViewAdapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.model.GalleryImageCard

class GalleryAdapter(private val values: List<GalleryImageCard>) :
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
    holder.galleryLayout?.setBackgroundColor(Color.parseColor(values[position].backgroundColor))
    holder.galleryImageView?.setImageBitmap(values[position].image)
    holder.galleryImageTime?.text = values[position].time
  }

  override fun getItemCount(): Int = values.size

  class GalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var galleryImageView: ImageView? = null
    var galleryImageTime: TextView? = null
    var galleryLayout: ConstraintLayout? = null

    init {
      galleryLayout = itemView.findViewById(R.id.cardLayout)

      galleryImageView = itemView.findViewById(R.id.cardImage)

      galleryImageTime = itemView.findViewById(R.id.cardTimeText)
    }

  }

}