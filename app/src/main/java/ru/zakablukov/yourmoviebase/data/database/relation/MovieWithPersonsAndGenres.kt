package ru.zakablukov.yourmoviebase.data.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.zakablukov.yourmoviebase.data.database.entity.GenreEntity
import ru.zakablukov.yourmoviebase.data.database.entity.MovieEntity
import ru.zakablukov.yourmoviebase.data.database.entity.MovieGenreCrossRef
import ru.zakablukov.yourmoviebase.data.database.entity.MoviePersonCrossRef
import ru.zakablukov.yourmoviebase.data.database.entity.PersonEntity

data class MovieWithPersonsAndGenres(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "personId",
        associateBy = Junction(MoviePersonCrossRef::class)
    )
    val persons: List<PersonEntity>,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
)