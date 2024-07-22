package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.AuthRepositoryImpl
import ru.zakablukov.yourmoviebase.data.repositoryimpl.DatabaseRepositoryImpl
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GenresRepositoryImpl
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val genresRepositoryImpl: GenresRepositoryImpl,
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val databaseRepositoryImpl: DatabaseRepositoryImpl,
) : ViewModel() {

    private val _apiGenresLoadState = MutableStateFlow<LoadState?>(null)
    val apiGenresLoadState: StateFlow<LoadState?> = _apiGenresLoadState
    private val _genresUpsertLoadState = MutableStateFlow<LoadState?>(null)
    val genresUpsertLoadState: StateFlow<LoadState?> = _genresUpsertLoadState

    private val _userResult = MutableStateFlow<FirebaseUser?>(null)
    val userResult: StateFlow<FirebaseUser?> = _userResult

    fun loadOrRefreshGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            genresRepositoryImpl.getAllGenres().collect { requestState ->
                when (requestState) {
                    is Request.Error -> _apiGenresLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _apiGenresLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _apiGenresLoadState.emit(LoadState.SUCCESS)
                        databaseRepositoryImpl.upsertAllGenres(requestState.data)
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

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.getCurrentUser().collect { requestState ->
                if (requestState is Request.Success) {
                    _userResult.emit(requestState.data)
                }
            }
        }
    }

    fun requestEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.requestEmailVerification()
        }
    }
}