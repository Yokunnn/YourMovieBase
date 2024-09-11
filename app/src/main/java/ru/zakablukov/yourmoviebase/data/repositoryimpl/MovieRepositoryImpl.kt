package ru.zakablukov.yourmoviebase.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.pagingsource.GalleryPagingSource
import ru.zakablukov.yourmoviebase.data.service.GenresService
import ru.zakablukov.yourmoviebase.data.service.MovieDetailsService
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val galleryPagingSource: GalleryPagingSource,
    private val movieDetailsService: MovieDetailsService,
    private val genresService: GenresService
) : MovieRepository {

    override fun getMovies(filterData: FilterData): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            galleryPagingSource.filterData = filterData
            galleryPagingSource
        }
    ).flow

    override suspend fun getMovieById(id: Int): Flow<Request<Movie>> {
        return requestFlow {
            val movie = movieDetailsService.getMovieById(id)
            movie.toDomain()
        }
    }

    override suspend fun getAllGenres(): Flow<Request<List<Genre>>> {
        return requestFlow {
            val genres = genresService.getAllGenres()
            genres.map { it.toDomain() }
        }
    }
}