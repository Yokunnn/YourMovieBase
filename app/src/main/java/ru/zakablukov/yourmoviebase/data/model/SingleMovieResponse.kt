package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class SingleMovieResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("alternativeName")
    val name: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("movieLength")
    val length: Int,
    @SerializedName("ageRating")
    val ageRating: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("poster")
    val poster: Poster
)
