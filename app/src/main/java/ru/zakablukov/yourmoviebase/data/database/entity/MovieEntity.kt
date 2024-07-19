package ru.zakablukov.yourmoviebase.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movie", indices = [Index(value = ["externalId"], unique = true)])
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    val externalId: Int,
    val name: String,
    val altName: String,
    val year: Int,
    val description: String,
    val length: Int,
    val ageRating: Int,
    val rating: Double,
    val posterUrl: String,
    val isFavourite: Boolean,
)
