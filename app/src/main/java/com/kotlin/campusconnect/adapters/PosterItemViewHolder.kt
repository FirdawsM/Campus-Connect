package com.kotlin.campusconnect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.campusconnect.databinding.ItemPosterBinding
import com.kotlin.campusconnect.models.Poster

class PosterItemViewHolder(
    private val binding: ItemPosterBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(poster: Poster, onLongClick: (Poster) -> Boolean) {
        binding.tvTitle.text = poster.title
        binding.tvDescription.text = poster.description
        binding.tvUserName.text = poster.userName

        binding.root.setOnLongClickListener { onLongClick(poster) }
    }
}