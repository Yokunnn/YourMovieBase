package ru.zakablukov.yourmoviebase.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zakablukov.yourmoviebase.data.database.dao.MovieDao
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.mapper.toEntity
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.Genre
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

    override fun getFavouriteMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            movieDao.getMoviesWithPersonsAndGenres()
        }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
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

    override suspend fun getAllGenres(): Flow<Request<List<Genre>>> {
        return requestFlow {
            val genres = movieDao.getGenres()
            genres.map { it.toDomain() }
        }
    }

    override suspend fun upsertAllGenres(genres: List<Genre>): Flow<Request<Unit>> {
        return requestFlow {
            val genreEntities = genres.map { it.toEntity() }
            movieDao.upsertAllGenres(*genreEntities.toTypedArray())
        }
    }
}