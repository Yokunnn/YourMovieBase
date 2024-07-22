package ru.zakablukov.yourmoviebase.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface DatabaseRepository {

    suspend fun upsertMovie(movie: Movie, isFavourite: Boolean): Flow<Request<Unit>>

    fun getFavouriteMovies(): Flow<PagingData<Movie>>

    suspend fun getMovieByExternalId(externalId: Int): Flow<Request<Movie>>

    suspend fun checkMovieFavourite(externalId: Int): Flow<Request<Boolean>>

    suspend fun updateIsFavouriteById(externalId: Int, isFavourite: Boolean): Flow<Request<Int>>

    suspend fun getAllGenres(): Flow<Request<List<Genre>>>

    suspend fun upsertAllGenres(genres: List<Genre>): Flow<Request<Unit>>
}