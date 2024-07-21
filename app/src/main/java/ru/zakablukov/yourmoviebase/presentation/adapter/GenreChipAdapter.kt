package ru.zakablukov.yourmoviebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.zakablukov.yourmoviebase.databinding.ItemGenreChipBinding
import ru.zakablukov.yourmoviebase.domain.model.Genre

class GenreChipAdapter : RecyclerView.Adapter<GenreChipAdapter.GenreViewHolder>() {

    private var items: MutableList<Genre> = emptyList<Genre>().toMutableList()

    inner class GenreViewHolder(
        binding: ItemGenreChipBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val genreChip = binding.genreChip
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreChipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenreViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val data = items[position]

        holder.genreChip.text = data.name
    }

    fun update(data: MutableList<Genre>) {
        items.addAll(data)
        notifyItemRangeInserted(items.size - 1, data.size)
    }
}