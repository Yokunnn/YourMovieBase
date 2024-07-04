package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.model.SingleMovieResponse
import ru.zakablukov.yourmoviebase.domain.model.Movie

fun SingleMovieResponse.toDomain(): Movie =
    Movie(
        id,
        name,
        year,
        description,
        length,
        ageRating,
        genres.map { it.name },
        rating.imdbRating,
        poster.url
    )