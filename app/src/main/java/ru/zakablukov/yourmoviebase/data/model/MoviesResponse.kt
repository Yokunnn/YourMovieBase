package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("docs")
    val movies: List<SingleMovieResponse>,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("prev")
    val prev: String,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPrev")
    val hasPrev: Boolean,
    @SerializedName("total")
    val total: Int,
)
