package ru.zakablukov.yourmoviebase.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface MovieDetailsRepository {

    suspend fun getMovieById(id: Int): Flow<Request<Movie>>
}