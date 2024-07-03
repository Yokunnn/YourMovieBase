package ru.zakablukov.yourmoviebase.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.service.GalleryService
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryService: GalleryService
) : GalleryRepository {

    override suspend fun getMovies(page: Int, limit: Int): Flow<Request<List<Movie>>> {
        return requestFlow {
            val movies = galleryService.getMovies(page, limit)
            movies.toDomain()
        }
    }
}