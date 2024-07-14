package ru.zakablukov.yourmoviebase.domain.model

data class FilterData(
    val genres: List<String>? = null,
    val rating: String? = null,
    val year: String? = null,
    val length: String? = null
)
