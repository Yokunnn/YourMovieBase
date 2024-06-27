package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("kp")
    val kinopoiskRating: Int,
    @SerializedName("imdb")
    val imdbRating: Int,
    @SerializedName("tmdb")
    val tmdbRating: Int
)
