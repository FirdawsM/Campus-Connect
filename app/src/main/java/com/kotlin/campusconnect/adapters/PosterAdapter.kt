package com.kotlin.campusconnect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.campusconnect.databinding.ItemPosterBinding
import com.kotlin.campusconnect.models.Poster

class PosterAdapter : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Poster>() {
        override fun areItemsTheSame(oldItem: Poster, newItem: Poster) = oldItem.createdAt == newItem.createdAt
        override fun areContentsTheSame(oldItem: Poster, newItem: Poster) = oldItem == newItem
    }
    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Poster) -> Unit)? = null
    private var onLongClickListener: ((Poster) -> Boolean)? = null

    fun setOnItemClickListener(listener: (Poster) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnLongClickListener(listener: (Poster) -> Boolean) {
        onLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val poster = differ.currentList[position]
        holder.bind(poster)
    }

    override fun getItemCount() = differ.currentList.size

    inner class PosterViewHolder(private val binding: ItemPosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(poster: Poster) {
            binding.tvTitle.text = poster.title
            binding.tvDescription.text = poster.description
            binding.tvUserName.text = poster.userName

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(poster)
            }

            binding.root.setOnLongClickListener {
                onLongClickListener?.invoke(poster) ?: false
            }
        }
    }
}