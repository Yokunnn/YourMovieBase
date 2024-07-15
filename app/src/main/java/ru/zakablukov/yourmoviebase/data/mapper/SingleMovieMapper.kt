package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.model.SingleMovieResponse
import ru.zakablukov.yourmoviebase.domain.model.Movie
import java.util.Locale

fun SingleMovieResponse.toDomain(): Movie =
    Movie(
        id,
        if (Locale.getDefault().language == "ru") name else altName,
        year,
        description,
        length,
        ageRating,
        genres.map { it.name },
        rating.imdbRating,
        poster.url,
        persons.filter { it.enProfession == "actor" }.map { it.toDomain() }
    )