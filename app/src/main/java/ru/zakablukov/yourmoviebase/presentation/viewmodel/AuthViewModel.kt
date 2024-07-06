package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.AuthRepositoryImpl
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {

    private val _signInLoadState = MutableStateFlow<LoadState?>(null)
    val signInLoadState: StateFlow<LoadState?> = _signInLoadState
    private val _registerLoadState = MutableStateFlow<LoadState?>(null)
    val registerLoadState: StateFlow<LoadState?> = _registerLoadState

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.signInWithEmailAndPassword(email, password).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _signInLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _signInLoadState.emit(LoadState.LOADING)
                    is Request.Success -> _signInLoadState.emit(LoadState.SUCCESS)
                }
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.createUserWithEmailAndPassword(email, password).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _registerLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _registerLoadState.emit(LoadState.LOADING)
                    is Request.Success -> _registerLoadState.emit(LoadState.SUCCESS)
                }
            }
        }
    }
}