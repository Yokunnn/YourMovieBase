package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("kp")
    val kinopoiskRating: Double,
    @SerializedName("imdb")
    val imdbRating: Double
)
