package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.DatabaseRepositoryImpl
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
    private val translateRepositoryImpl: TranslateRepositoryImpl,
    private val databaseRepositoryImpl: DatabaseRepositoryImpl,
) : ViewModel() {

    private val _movieDetailsResult = MutableStateFlow<Movie?>(null)
    val movieDetailsResult: StateFlow<Movie?> = _movieDetailsResult
    private val _movieApiDetailsLoadState = MutableStateFlow<LoadState?>(null)
    val movieApiDetailsLoadState: StateFlow<LoadState?> = _movieApiDetailsLoadState
    private val _movieLocalDetailsLoadState = MutableStateFlow<LoadState?>(null)
    val movieLocalDetailsLoadState: StateFlow<LoadState?> = _movieLocalDetailsLoadState

    private val _movieUpsertLocalLoadState = MutableStateFlow<LoadState?>(null)
    val movieUpsertLocalLoadState: StateFlow<LoadState?> = _movieUpsertLocalLoadState

    private val _isFavouriteLoadState = MutableStateFlow<LoadState?>(null)
    val isFavouriteLoadState: StateFlow<LoadState?> = _isFavouriteLoadState
    private val _isFavouriteResult = MutableStateFlow<Boolean?>(null)
    val isFavouriteResult: StateFlow<Boolean?> = _isFavouriteResult

    private val _translatedTextResult = MutableStateFlow<TranslateText?>(null)
    val translatedTextResult: StateFlow<TranslateText?> = _translatedTextResult
    private val _translatedTextLoadState = MutableStateFlow<LoadState?>(null)
    val translatedTextLoadState: StateFlow<LoadState?> = _translatedTextLoadState

    fun getMovieById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepositoryImpl.getMovieByExternalId(id).collect { requestState ->
                when (requestState) {
                    is Request.Error -> {
                        _movieLocalDetailsLoadState.emit(LoadState.ERROR)
                        getApiMovieById(id)
                    }

                    is Request.Loading -> _movieLocalDetailsLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _movieLocalDetailsLoadState.emit(LoadState.SUCCESS)
                        _movieDetailsResult.emit(requestState.data)
                        checkMovieFavourite()
                    }
                }
            }
        }
    }

    private suspend fun getApiMovieById(id: Int) {
        movieDetailsRepositoryImpl.getMovieById(id).collect { requestState ->
            when (requestState) {
                is Request.Error -> _movieApiDetailsLoadState.emit(LoadState.ERROR)
                is Request.Loading -> _movieApiDetailsLoadState.emit(LoadState.LOADING)
                is Request.Success -> {
                    _movieApiDetailsLoadState.emit(LoadState.SUCCESS)
                    _movieDetailsResult.emit(requestState.data)
                    upsertMovieLocal(requestState.data, false)
                }
            }
        }
    }

    private suspend fun upsertMovieLocal(movie: Movie, isFavourite: Boolean) {
        databaseRepositoryImpl.upsertMovie(movie, isFavourite).collect { requestState ->
            when (requestState) {
                is Request.Error -> _movieUpsertLocalLoadState.emit(LoadState.ERROR)
                is Request.Loading -> _movieUpsertLocalLoadState.emit(LoadState.LOADING)
                is Request.Success -> _movieUpsertLocalLoadState.emit(LoadState.SUCCESS)
            }
        }
    }

    private suspend fun checkMovieFavourite() {
        _movieDetailsResult.value?.let {
            databaseRepositoryImpl.checkMovieFavourite(it.id).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _isFavouriteLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _isFavouriteLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _isFavouriteLoadState.emit(LoadState.SUCCESS)
                        _isFavouriteResult.emit(requestState.data)
                    }
                }
            }
        }
    }

    fun updateIsFavouriteById(isFavourite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _movieDetailsResult.value?.let {
                databaseRepositoryImpl.updateIsFavouriteById(it.id, isFavourite)
                    .collect { requestState ->
                        when (requestState) {
                            is Request.Error -> _isFavouriteLoadState.emit(LoadState.ERROR)
                            is Request.Loading -> _isFavouriteLoadState.emit(LoadState.LOADING)
                            is Request.Success -> {
                                _isFavouriteLoadState.emit(LoadState.SUCCESS)
                                checkMovieFavourite()
                            }
                        }
                    }
            }
        }
    }

    fun translateRUtoEN(translateText: TranslateText) {
        viewModelScope.launch(Dispatchers.IO) {
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