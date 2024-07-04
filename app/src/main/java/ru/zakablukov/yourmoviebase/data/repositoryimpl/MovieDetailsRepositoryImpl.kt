package ru.zakablukov.yourmoviebase.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.service.MovieDetailsService
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val movieDetailsService: MovieDetailsService
) : MovieDetailsRepository {

    override suspend fun getMovieById(id: Int): Flow<Request<Movie>> {
        return requestFlow {
            val movie = movieDetailsService.getMovieById(id)
            movie.toDomain()
        }
    }
}