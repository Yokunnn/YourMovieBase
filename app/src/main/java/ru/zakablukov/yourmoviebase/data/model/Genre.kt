package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("name")
    val name: String,
)
