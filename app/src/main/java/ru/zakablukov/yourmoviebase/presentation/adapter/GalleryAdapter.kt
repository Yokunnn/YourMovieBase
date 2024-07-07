package ru.zakablukov.yourmoviebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.zakablukov.yourmoviebase.databinding.ItemGalleryMovieBinding
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.presentation.adapter.itemcallback.MovieDiffCallback

class GalleryAdapter(
    private val onItemClicked: (Movie?) -> Unit
) : PagingDataAdapter<Movie, GalleryAdapter.GalleryViewHolder>(MovieDiffCallback()) {

    inner class GalleryViewHolder(
        binding: ItemGalleryMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val posterImageView = binding.posterImageView
        val nameTextView = binding.nameTextView
        val ratingTextView = binding.ratingTextView

        init {
            itemView.setOnClickListener {
                onItemClicked(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val data = getItem(position)

        with(holder) {
            nameTextView.text = data?.name
            ratingTextView.text = data?.rating.toString()
            Glide.with(posterImageView.context).load(data?.posterUrl).into(posterImageView)
        }
    }
}