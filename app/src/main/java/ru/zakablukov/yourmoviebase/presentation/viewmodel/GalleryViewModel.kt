package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GalleryRepositoryImpl
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Movie
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepositoryImpl: GalleryRepositoryImpl
) : ViewModel() {

    private val _filterData = MutableStateFlow(FilterData())
    val filterData: StateFlow<FilterData> = _filterData
    private val _moviesResult = MutableStateFlow<PagingData<Movie>?>(PagingData.empty())
    val moviesResult: StateFlow<PagingData<Movie>?> = _moviesResult

    fun applyFilters(filterData: FilterData) {
        viewModelScope.launch(Dispatchers.IO) {
            _filterData.emit(filterData)
        }
    }

    fun resetFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            _filterData.emit(FilterData())
        }
    }

    suspend fun refreshMovies() {
        _moviesResult.emit(getPagingData())
    }

    private suspend fun getPagingData(): PagingData<Movie>?{
        return galleryRepositoryImpl.getMovies(_filterData.value)
            .cachedIn(viewModelScope).firstOrNull()
    }
}