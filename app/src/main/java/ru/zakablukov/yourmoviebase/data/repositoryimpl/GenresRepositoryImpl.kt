package ru.zakablukov.yourmoviebase.data.repositoryimpl

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.service.GenresService
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.repository.GenresRepository
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(
    private val genresService: GenresService
) : GenresRepository{

    override suspend fun getAllGenres(): Flow<Request<List<Genre>>> {
        return requestFlow {
            val genres = genresService.getAllGenres()
            genres.map { it.toDomain() }
        }
    }
}