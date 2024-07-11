package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.MovieDetailsRepositoryImpl
import ru.zakablukov.yourmoviebase.data.repositoryimpl.TranslateRepositoryImpl
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.model.TranslateText
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsRepositoryImpl: MovieDetailsRepositoryImpl,
    private val translateRepositoryImpl: TranslateRepositoryImpl
) : ViewModel() {

    private val _movieDetailsResult = MutableStateFlow<Movie?>(null)
    val movieDetailsResult: StateFlow<Movie?> = _movieDetailsResult
    private val _movieDetailsLoadState = MutableStateFlow<LoadState?>(null)
    val movieDetailsLoadState: StateFlow<LoadState?> = _movieDetailsLoadState
    private val _translatedTextResult = MutableStateFlow<TranslateText?>(null)
    val translatedTextResult: StateFlow<TranslateText?> = _translatedTextResult
    private val _translatedTextLoadState = MutableStateFlow<LoadState?>(null)
    val translatedTextLoadState: StateFlow<LoadState?> = _translatedTextLoadState

    fun getMovieById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDetailsRepositoryImpl.getMovieById(id).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _movieDetailsLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _movieDetailsLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _movieDetailsLoadState.emit(LoadState.SUCCESS)
                        _movieDetailsResult.emit(requestState.data)
                    }
                }
            }
        }
    }

    fun translateRUtoEN(translateText: TranslateText) {
        viewModelScope.launch {
            translateRepositoryImpl.translateRUtoEN(translateText).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _translatedTextLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _translatedTextLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _translatedTextLoadState.emit(LoadState.SUCCESS)
                        _translatedTextResult.emit(requestState.data)
                    }
                }
            }
        }
    }
}