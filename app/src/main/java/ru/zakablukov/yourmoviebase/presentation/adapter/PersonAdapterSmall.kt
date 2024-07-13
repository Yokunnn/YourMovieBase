package ru.zakablukov.yourmoviebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.zakablukov.yourmoviebase.databinding.ItemPersonSmallBinding
import ru.zakablukov.yourmoviebase.domain.model.Person

class PersonAdapterSmall : RecyclerView.Adapter<PersonAdapterSmall.PersonSmallViewHolder>() {

    private var items: MutableList<Person> = emptyList<Person>().toMutableList()

    inner class PersonSmallViewHolder(
        binding: ItemPersonSmallBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val photoImageView = binding.photoImageView
        val nameTextView = binding.nameTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonSmallViewHolder {
        val binding = ItemPersonSmallBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PersonSmallViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PersonSmallViewHolder, position: Int) {
        val data = items[position]

        with(holder) {
            nameTextView.text = data.name
            Glide.with(photoImageView.context).load(data.photoUrl).into(photoImageView)
        }
    }

    fun update(data: MutableList<Person>) {
        items.addAll(data)
        notifyItemRangeInserted(items.size - 1, data.size)
    }
}