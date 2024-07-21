package ru.zakablukov.yourmoviebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.toImmutableList
import ru.zakablukov.yourmoviebase.databinding.ItemGenreChipBinding
import ru.zakablukov.yourmoviebase.domain.model.Genre
import java.util.Locale

class GenreChipAdapter : RecyclerView.Adapter<GenreChipAdapter.GenreViewHolder>() {

    private var items: MutableList<Genre> = emptyList<Genre>().toMutableList()
    private var itemsEN: MutableList<Genre> = emptyList<Genre>().toMutableList()
    private var checked: MutableList<Genre> = emptyList<Genre>().toMutableList()

    inner class GenreViewHolder(
        binding: ItemGenreChipBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val genreChip = binding.genreChip

        init {
            binding.genreChip.setOnCheckedChangeListener { _, isChecked ->
                val genre = if (Locale.getDefault().language == "ru")
                    Genre(binding.genreChip.text.toString())
                else
                    items[itemsEN.indexOf(Genre(binding.genreChip.text.toString()))]
                if (isChecked) {
                    checked.add(genre)
                } else {
                    checked.remove(genre)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreChipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.genreChip.isCheckable = true
        return GenreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (Locale.getDefault().language == "ru")
            items.size
        else
            itemsEN.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        if (Locale.getDefault().language == "ru") {
            holder.genreChip.text = items[position].name
        } else {
            holder.genreChip.text = itemsEN[position].name
        }
        holder.genreChip.isChecked = checked.contains(items[position])
    }

    fun update(data: MutableList<Genre>) {
        items.addAll(data)
        notifyItemRangeInserted(items.size - 1, data.size)
    }

    fun updateEN(data: MutableList<Genre>) {
        itemsEN.addAll(data)
        notifyItemRangeInserted(itemsEN.size - 1, data.size)
    }

    fun getCheckedGenres() = checked.toImmutableList().map { it.name }

    fun insertCheckedGenres(checkedGenres: List<String>) {
        checked.clear()
        checked.addAll(checkedGenres.map { Genre(it) })
    }
}