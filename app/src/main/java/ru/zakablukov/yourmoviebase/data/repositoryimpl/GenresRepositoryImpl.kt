package ru.zakablukov.yourmoviebase.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.database.dao.MovieDao
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.mapper.toEntity
import ru.zakablukov.yourmoviebase.data.service.GenresService
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.repository.GenresRepository
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(
    private val genresService: GenresService,
    private val movieDao: MovieDao
) : GenresRepository{

    override suspend fun getAllGenres(): Flow<Request<List<Genre>>> {
        return requestFlow {
            val genres = genresService.getAllGenres()
            genres.map { it.toDomain() }
        }
    }

    override suspend fun getAllLocalGenres(): Flow<Request<List<Genre>>> {
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