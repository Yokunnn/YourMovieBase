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
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GenresRepositoryImpl
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepositoryImpl: GalleryRepositoryImpl,
    private val genresRepositoryImpl: GenresRepositoryImpl,
) : ViewModel() {

    private val _filterData = MutableStateFlow(FilterData())
    val filterData: StateFlow<FilterData> = _filterData
    private val _moviesResult = MutableStateFlow<PagingData<Movie>?>(PagingData.empty())
    val moviesResult: StateFlow<PagingData<Movie>?> = _moviesResult
    private val _genresLocalLoadState = MutableStateFlow<LoadState?>(null)
    val genresLocalLoadState: StateFlow<LoadState?> = _genresLocalLoadState
    private val _genresApiLoadState = MutableStateFlow<LoadState?>(null)
    val genresApiLoadState: StateFlow<LoadState?> = _genresApiLoadState
    private val _genresResult = MutableStateFlow<List<Genre>>(emptyList())
    val genresResult: StateFlow<List<Genre>> = _genresResult

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

    private suspend fun getPagingData(): PagingData<Movie>? {
        return galleryRepositoryImpl.getMovies(_filterData.value)
            .cachedIn(viewModelScope).firstOrNull()
    }

    fun getAllLocalGenres() {
        viewModelScope.launch {
            genresRepositoryImpl.getAllLocalGenres().collect { requestState ->
                when (requestState) {
                    is Request.Error -> {
                        _genresLocalLoadState.emit(LoadState.ERROR)
                        getAndSaveGenres()
                    }

                    is Request.Loading -> _genresLocalLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _genresLocalLoadState.emit(LoadState.SUCCESS)
                        _genresResult.emit(requestState.data)
                    }
                }
            }
        }
    }

    private suspend fun getAndSaveGenres() {
        genresRepositoryImpl.getAllGenres().collect { requestState ->
            when (requestState) {
                is Request.Error -> _genresApiLoadState.emit(LoadState.ERROR)
                is Request.Loading -> _genresApiLoadState.emit(LoadState.LOADING)
                is Request.Success -> {
                    _genresApiLoadState.emit(LoadState.SUCCESS)
                    _genresResult.emit(requestState.data)
                    genresRepositoryImpl.upsertAllGenres(requestState.data)
                }
            }
        }
    }
}