package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("alternativeName")
    val name: String,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("poster")
    val poster: Poster
)
