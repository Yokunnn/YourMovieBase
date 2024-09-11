package ru.zakablukov.yourmoviebase.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.model.Movie

interface MovieRepository {

    fun getMovies(filterData: FilterData): Flow<PagingData<Movie>>

    suspend fun getMovieById(id: Int): Flow<Request<Movie>>

    suspend fun getAllGenres(): Flow<Request<List<Genre>>>
}