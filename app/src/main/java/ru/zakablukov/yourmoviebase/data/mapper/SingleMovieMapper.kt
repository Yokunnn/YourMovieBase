package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.database.entity.MovieEntity
import ru.zakablukov.yourmoviebase.data.database.relation.MovieWithPersonsAndGenres
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
        genres.map { it.toDomain() },
        rating.imdbRating,
        poster.url,
        persons.filter { it.enProfession == "actor" }.map { it.toDomain() }
    )

fun Movie.toEntity(isFavourite: Boolean): MovieWithPersonsAndGenres =
    MovieWithPersonsAndGenres(
        MovieEntity(
            externalId = id,
            name = name,
            altName = name,
            year = year,
            description = description,
            length = length,
            ageRating = ageRating,
            rating = rating,
            posterUrl = posterUrl,
            isFavourite = isFavourite
        ),
        persons.map { it.toEntity() },
        genres.map { it.toEntity() }
    )

fun MovieWithPersonsAndGenres.toDomain(): Movie =
    Movie(
        movie.externalId,
        if (Locale.getDefault().language == "ru") movie.name else movie.altName,
        movie.year,
        movie.description,
        movie.length,
        movie.ageRating,
        genres.map { it.toDomain() },
        movie.rating,
        movie.posterUrl,
        persons.map { it.toDomain() }
    )