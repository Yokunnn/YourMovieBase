package ru.zakablukov.yourmoviebase.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ru.zakablukov.yourmoviebase.data.database.entity.GenreEntity
import ru.zakablukov.yourmoviebase.data.database.entity.MovieEntity
import ru.zakablukov.yourmoviebase.data.database.entity.MovieGenreCrossRef
import ru.zakablukov.yourmoviebase.data.database.entity.MoviePersonCrossRef
import ru.zakablukov.yourmoviebase.data.database.entity.PersonEntity
import ru.zakablukov.yourmoviebase.data.database.relation.MovieWithPersonsAndGenres

@Dao
interface MovieDao {

    @Transaction
    @Query(SELECT_FULL_MOVIES)
    fun getMoviesWithPersonsAndGenres(): PagingSource<Int, MovieWithPersonsAndGenres>

    @Transaction
    @Query(SELECT_MOVIE_BY_ID)
    suspend fun getMovieByExternalId(externalId: Int): MovieWithPersonsAndGenres

    @Query(SELECT_MOVIE_FAVOURITE_BY_ID)
    suspend fun checkMovieFavourite(externalId: Int): Boolean

    @Query(SELECT_ALL_GENRES)
    suspend fun getGenres(): List<GenreEntity>

    @Transaction
    suspend fun upsertMovieWithPersonsAndGenres(movieWithPersonsAndGenres: MovieWithPersonsAndGenres) {
        val movieId = upsertMovie(movieWithPersonsAndGenres.movie).toInt()
        movieWithPersonsAndGenres.persons.forEach { person ->
            val personId = upsertPerson(person).toInt()
            insertMoviePersonCrossRef(MoviePersonCrossRef(movieId, personId))
        }
        movieWithPersonsAndGenres.genres.forEach { genre ->
            val genreId = upsertGenre(genre).toInt()
            insertMovieGenreCrossRef(MovieGenreCrossRef(movieId, genreId))
        }
    }

    @Upsert
    suspend fun upsertMovie(movieEntity: MovieEntity): Long

    @Query(UPDATE_FAVOURITE_BY_ID)
    suspend fun updateIsFavouriteById(externalId: Int, isFavourite: Boolean): Int

    @Upsert
    suspend fun upsertGenre(genreEntity: GenreEntity): Long

    @Upsert
    suspend fun upsertAllGenres(vararg genreEntity: GenreEntity)

    @Upsert
    suspend fun upsertPerson(personEntity: PersonEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRef(movieGenreCrossRef: MovieGenreCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePersonCrossRef(moviePersonCrossRef: MoviePersonCrossRef)

    companion object {
        private const val SELECT_FULL_MOVIES = "SELECT * FROM movie WHERE isFavourite = true"
        private const val SELECT_MOVIE_BY_ID = "SELECT * FROM movie WHERE externalId LIKE :externalId"
        private const val SELECT_MOVIE_FAVOURITE_BY_ID = "SELECT isFavourite FROM movie WHERE externalId LIKE :externalId"
        private const val SELECT_ALL_GENRES = "SELECT * FROM genre"
        private const val UPDATE_FAVOURITE_BY_ID = "UPDATE movie SET isFavourite = :isFavourite WHERE externalId LIKE :externalId"
    }
}