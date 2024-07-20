package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.database.entity.GenreEntity
import ru.zakablukov.yourmoviebase.data.model.GenreResponse
import ru.zakablukov.yourmoviebase.domain.model.Genre

fun GenreResponse.toDomain(): Genre =
    Genre(name)

fun Genre.toEntity(): GenreEntity =
    GenreEntity(name = name)