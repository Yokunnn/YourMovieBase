package ru.zakablukov.yourmoviebase.data.database.entity

import androidx.room.Entity

@Entity(tableName = "movie_person", primaryKeys = ["movieId", "personId"])
data class MoviePersonCrossRef(
    val movieId: Int,
    val personId: Int,
)
