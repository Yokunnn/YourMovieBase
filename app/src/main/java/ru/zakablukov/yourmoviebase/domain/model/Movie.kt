package ru.zakablukov.yourmoviebase.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val rating: Double,
    val posterUrl: String
)
