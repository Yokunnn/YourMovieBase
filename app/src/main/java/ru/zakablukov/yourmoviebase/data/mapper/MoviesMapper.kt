package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.model.MoviesResponse
import ru.zakablukov.yourmoviebase.domain.model.Movie

fun MoviesResponse.mapToDomain(): List<Movie> {
    val values = emptyList<Movie>().toMutableList()
    movies.forEach {
        values.add(
            Movie(
                it.id,
                it.name,
                it.rating.imdbRating,
                it.poster.url
            )
        )
    }
    return values
}