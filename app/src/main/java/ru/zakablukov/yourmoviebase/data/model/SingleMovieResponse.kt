package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class SingleMovieResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("alternativeName")
    val altName: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("movieLength")
    val length: Int,
    @SerializedName("ageRating")
    val ageRating: Int,
    @SerializedName("genres")
    val genres: List<GenreResponse>,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("poster")
    val poster: Poster,
    @SerializedName("persons")
    val persons: List<PersonResponse>
)
