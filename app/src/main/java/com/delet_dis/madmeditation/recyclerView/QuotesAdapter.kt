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
import com.delet_dis.madmeditation.model.Quote

class QuotesAdapter(private val values: List<Quote>) :
  RecyclerView.Adapter<QuotesAdapter.QuotesHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): QuotesHolder {

    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.quotes_recyclerview_item, parent, false)

    return QuotesHolder(itemView)
  }

  override fun onBindViewHolder(holder: QuotesHolder, position: Int) {
    holder.quoteTitle?.text = values[position].title

    holder.quoteDescription?.text = values[position].description

    holder.quoteImage?.let {
      Glide.with(holder.itemView)
        .load(values[position].image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(it)
    }
  }

  override fun getItemCount(): Int = values.size

  class QuotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var quoteTitle: TextView? = null
    var quoteDescription: TextView? = null

    var quoteImage: ImageView? = null

    init {
      quoteTitle = itemView.findViewById(R.id.quoteTitle)
      quoteDescription = itemView.findViewById(R.id.quoteDesctiption)

      quoteImage = itemView.findViewById(R.id.quoteImage)
    }
  }

}