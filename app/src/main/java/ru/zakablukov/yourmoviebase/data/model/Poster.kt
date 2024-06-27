package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("url")
    val url: String,
    @SerializedName("previewUrl")
    val previewUrl: String
)
