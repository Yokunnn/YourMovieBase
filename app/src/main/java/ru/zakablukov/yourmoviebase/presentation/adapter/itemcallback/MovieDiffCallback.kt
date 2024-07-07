package ru.zakablukov.yourmoviebase.presentation.adapter.itemcallback

import androidx.recyclerview.widget.DiffUtil
import ru.zakablukov.yourmoviebase.domain.model.Movie

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}