package ru.zakablukov.yourmoviebase.data.model

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("name")
    val name: String,
)
