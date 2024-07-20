package ru.zakablukov.yourmoviebase.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val year: Int,
    val description: String,
    val length: Int,
    val ageRating: Int,
    val genres: List<Genre>,
    val rating: Double,
    val posterUrl: String,
    val persons: List<Person>
)
