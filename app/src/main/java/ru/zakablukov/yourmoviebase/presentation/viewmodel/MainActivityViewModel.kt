package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GenresRepositoryImpl
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val genresRepositoryImpl: GenresRepositoryImpl,
) : ViewModel() {

    private val _apiGenresLoadState = MutableStateFlow<LoadState?>(null)
    val apiGenresLoadState: StateFlow<LoadState?> = _apiGenresLoadState
    private val _genresUpsertLoadState = MutableStateFlow<LoadState?>(null)
    val genresUpsertLoadState: StateFlow<LoadState?> = _genresUpsertLoadState

    fun loadOrRefreshGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            genresRepositoryImpl.getAllGenres().collect { requestState ->
                when (requestState) {
                    is Request.Error -> _apiGenresLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _apiGenresLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _apiGenresLoadState.emit(LoadState.SUCCESS)
                        genresRepositoryImpl.upsertAllGenres(requestState.data)
                            .collect { dbRequestState ->
                                when (dbRequestState) {
                                    is Request.Error -> _genresUpsertLoadState.emit(LoadState.ERROR)
                                    is Request.Loading -> _genresUpsertLoadState.emit(LoadState.LOADING)
                                    is Request.Success -> _genresUpsertLoadState.emit(LoadState.SUCCESS)
                                }
                            }
                    }
                }
            }
        }
    }
}