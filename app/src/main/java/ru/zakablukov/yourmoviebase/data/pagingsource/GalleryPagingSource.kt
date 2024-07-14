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
) : PagingSource<Int, Movie>() {

    var filterData = FilterData()

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = galleryService.getMovies(
                page,
                params.loadSize,
                filterData.rating?.let { listOf(it) },
                filterData.year?.let { listOf(it) },
                filterData.length?.let { listOf(it) }
            )
            val movies = response.toDomain()
            LoadResult.Page(
                movies,
                if (page == 1) null else page - 1,
                if (movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}