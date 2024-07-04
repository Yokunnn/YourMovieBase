package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("docs")
    val movies: List<SingleMovieResponse>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int
)
