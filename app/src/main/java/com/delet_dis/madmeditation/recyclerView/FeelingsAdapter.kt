package com.delet_dis.madmeditation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.R
import com.delet_dis.madmeditation.model.Feeling

class FeelingsAdapter(private val values: List<Feeling>) :
  RecyclerView.Adapter<FeelingsAdapter.FeelingsHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): FeelingsHolder {
    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.feelings_recyclerview_item, parent, false)

    return FeelingsHolder(itemView)
  }

  override fun onBindViewHolder(holder: FeelingsHolder, position: Int) {
    holder.feelingName?.text = values[position].title

    holder.feelingImage?.let {
      Glide.with(holder.itemView)
        .load(values[position].image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(it)
    }
  }

  override fun getItemCount(): Int = values.size

  class FeelingsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var feelingName: TextView? = null
    var feelingImage: ImageView? = null

    init {
      feelingImage = itemView.findViewById(R.id.feelingImage)

      feelingName = itemView.findViewById(R.id.feelingName)
    }
  }

}