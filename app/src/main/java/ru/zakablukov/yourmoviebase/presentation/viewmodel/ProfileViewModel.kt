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
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {

    private val _signOutLoadState = MutableStateFlow<LoadState?>(null)
    val signOutLoadState: StateFlow<LoadState?> = _signOutLoadState
    private val _userResult = MutableStateFlow<FirebaseUser?>(null)
    val userResult: StateFlow<FirebaseUser?> = _userResult

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.signOut().collect { requestState ->
                when (requestState) {
                    is Request.Error -> _signOutLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _signOutLoadState.emit(LoadState.LOADING)
                    is Request.Success -> _signOutLoadState.emit(LoadState.SUCCESS)
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
}