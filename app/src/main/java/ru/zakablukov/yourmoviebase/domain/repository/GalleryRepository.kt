package ru.zakablukov.yourmoviebase.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface GalleryRepository {

    suspend fun getMovies(page: Int, limit: Int): Flow<Request<List<Movie>>>
}