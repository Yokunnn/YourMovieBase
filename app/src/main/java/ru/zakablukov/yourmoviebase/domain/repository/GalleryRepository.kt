package ru.zakablukov.yourmoviebase.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface GalleryRepository {

    fun getMovies(filterData: FilterData): Flow<PagingData<Movie>>
}