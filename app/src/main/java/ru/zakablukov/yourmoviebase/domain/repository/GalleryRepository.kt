package ru.zakablukov.yourmoviebase.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface GalleryRepository {

    fun getMovies(): Flow<PagingData<Movie>>
}