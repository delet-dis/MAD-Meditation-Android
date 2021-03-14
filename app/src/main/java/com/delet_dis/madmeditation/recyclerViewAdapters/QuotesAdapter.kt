package com.delet_dis.madmeditation.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.delet_dis.madmeditation.databinding.QuotesRecyclerviewItemBinding
import com.delet_dis.madmeditation.model.Quote

class QuotesAdapter(private val values: List<Quote>) :
  RecyclerView.Adapter<QuotesAdapter.QuotesHolder>() {
  private lateinit var binding: QuotesRecyclerviewItemBinding

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): QuotesHolder {

    binding = QuotesRecyclerviewItemBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )

    return QuotesHolder(binding)
  }

  override fun onBindViewHolder(holder: QuotesHolder, position: Int) {
    with(values[position]) {
      binding.quoteTitle.text = title

      binding.quoteDescription.text = description

      binding.quoteImage.let {
        Glide.with(holder.itemView)
          .load(values[position].image)
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(it)
      }
    }
  }

  override fun getItemCount(): Int = values.size

  inner class QuotesHolder(binding: QuotesRecyclerviewItemBinding) :
    RecyclerView.ViewHolder(binding.root)
}