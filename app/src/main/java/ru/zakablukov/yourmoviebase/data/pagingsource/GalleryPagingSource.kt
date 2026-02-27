package ru.zakablukov.yourmoviebase.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.zakablukov.yourmoviebase.data.mapper.toDomain
import ru.zakablukov.yourmoviebase.data.service.GalleryService
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Movie
import javax.inject.Inject

class GalleryPagingSource @Inject constructor(
    private val galleryService: GalleryService
) : PagingSource<String, Movie>() {

    var filterData = FilterData()

    override fun getRefreshKey(state: PagingState<String, Movie>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Movie> {
        return try {
            val next = params.key
            val response = galleryService.getMovies(
                next,
                params.loadSize,
                filterData.rating?.let { listOf(it) },
                filterData.year?.let { listOf(it) },
                filterData.length?.let { listOf(it) },
                filterData.genres
            )
            val movies = response.toDomain()
            LoadResult.Page(
                movies,
                response.prev,
                response.next
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}