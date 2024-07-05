package ru.zakablukov.yourmoviebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.zakablukov.yourmoviebase.databinding.ItemGalleryMovieBinding
import ru.zakablukov.yourmoviebase.domain.model.Movie

class GalleryAdapter(
    private val onItemClicked: (Movie) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private var items: MutableList<Movie> = emptyList<Movie>().toMutableList()

    inner class GalleryViewHolder(
        binding: ItemGalleryMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val posterImageView = binding.posterImageView
        val nameTextView = binding.nameTextView
        val ratingTextView = binding.ratingTextView

        init {
            itemView.setOnClickListener {
                onItemClicked(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val data = items[position]

        with(holder) {
            nameTextView.text = data.name
            ratingTextView.text = data.rating.toString()
            Glide.with(posterImageView.context).load(data.posterUrl).into(posterImageView)
        }
    }

    fun update(data: MutableList<Movie>) {
        items.addAll(data)
        notifyItemRangeInserted(items.size - 1, data.size)
    }
}