package ru.zakablukov.yourmoviebase.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Genre

interface GenresRepository {

    suspend fun getAllGenres(): Flow<Request<List<Genre>>>

    suspend fun getAllLocalGenres(): Flow<Request<List<Genre>>>

    suspend fun upsertAllGenres(genres: List<Genre>): Flow<Request<Unit>>
}