package ru.zakablukov.yourmoviebase.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.database.dao.MovieDao
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.mapper.toEntity
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.repository.DatabaseRepository
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
) : DatabaseRepository {

    override suspend fun upsertMovie(movie: Movie, isFavourite: Boolean): Flow<Request<Unit>> {
        return requestFlow {
            movieDao.upsertMovieWithPersonsAndGenres(movie.toEntity(isFavourite))
        }
    }

    override suspend fun getMovieByExternalId(externalId: Int): Flow<Request<Movie>> {
        return requestFlow {
            val movie = movieDao.getMovieByExternalId(externalId)
            movie.toDomain()
        }
    }

    override suspend fun checkMovieFavourite(externalId: Int): Flow<Request<Boolean>> {
        return requestFlow {
            movieDao.checkMovieFavourite(externalId)
        }
    }

    override suspend fun updateIsFavouriteById(
        externalId: Int,
        isFavourite: Boolean,
    ): Flow<Request<Int>> {
        return requestFlow {
            movieDao.updateIsFavouriteById(externalId, isFavourite)
        }
    }
}