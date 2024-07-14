package ru.zakablukov.yourmoviebase.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.pagingsource.GalleryPagingSource
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryPagingSource: GalleryPagingSource
) : GalleryRepository {

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
}