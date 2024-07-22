package ru.zakablukov.yourmoviebase.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface DatabaseRepository {

    suspend fun upsertMovie(movie: Movie, isFavourite: Boolean): Flow<Request<Unit>>

    suspend fun getMovieByExternalId(externalId: Int): Flow<Request<Movie>>

    suspend fun checkMovieFavourite(externalId: Int): Flow<Request<Boolean>>

    suspend fun updateIsFavouriteById(externalId: Int, isFavourite: Boolean): Flow<Request<Int>>
}