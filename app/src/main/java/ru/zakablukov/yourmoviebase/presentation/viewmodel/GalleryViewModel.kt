package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GalleryRepositoryImpl
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Movie
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepositoryImpl: GalleryRepositoryImpl
) : ViewModel() {

    private val _moviesResult = MutableStateFlow<List<Movie>?>(null)
    val moviesResult: StateFlow<List<Movie>?> = _moviesResult
    private val _moviesLoadState = MutableStateFlow<LoadState?>(null)
    val moviesLoadState: StateFlow<LoadState?> = _moviesLoadState

    fun getMovies(page: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            galleryRepositoryImpl.getMovies(page, limit).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _moviesLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _moviesLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _moviesLoadState.emit(LoadState.SUCCESS)
                        _moviesResult.emit(requestState.data)
                    }
                }
            }
        }
    }
}