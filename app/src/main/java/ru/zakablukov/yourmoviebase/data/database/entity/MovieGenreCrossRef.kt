package ru.zakablukov.yourmoviebase.data.database.entity

import androidx.room.Entity

@Entity(tableName = "movie_genre", primaryKeys = ["movieId", "genreId"])
data class MovieGenreCrossRef(
    val movieId: Int,
    val genreId: Int,
)
