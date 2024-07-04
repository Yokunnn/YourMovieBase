package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.model.MoviesResponse
import ru.zakablukov.yourmoviebase.domain.model.Movie

fun MoviesResponse.toDomain(): List<Movie> = movies.map {
    it.toDomain()
}