package ru.zakablukov.yourmoviebase.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
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
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val credentialManager: CredentialManager,
    private val googleCredentialRequest: GetCredentialRequest
) : ViewModel() {

    private val _signInLoadState = MutableStateFlow<LoadState?>(null)
    val signInLoadState: StateFlow<LoadState?> = _signInLoadState
    private val _registerLoadState = MutableStateFlow<LoadState?>(null)
    val registerLoadState: StateFlow<LoadState?> = _registerLoadState
    private val _signInGoogleLoadState = MutableStateFlow<LoadState?>(null)
    val signInGoogleLoadState: StateFlow<LoadState?> = _signInGoogleLoadState

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

    private fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.signInWithGoogle(credential).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _signInGoogleLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _signInGoogleLoadState.emit(LoadState.LOADING)
                    is Request.Success -> _signInGoogleLoadState.emit(LoadState.SUCCESS)
                }
            }
        }
    }

    fun requestEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepositoryImpl.requestEmailVerification()
        }
    }

    private fun handleGoogleSignIn(result: GetCredentialResponse?) {
        val credential = result?.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential =
                GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            signInWithGoogle(firebaseCredential)
        } else {
            Log.d(CREDENTIAL_TAG, "Unexpected type of credential")
        }
    }

    fun tryGetCredentials(context: Context) {
        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context, googleCredentialRequest
                )
                handleGoogleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e(CREDENTIAL_TAG, "type: ${e.type}, message: ${e.errorMessage}")
            }
        }
    }

    companion object {
        private const val CREDENTIAL_TAG = "Credentials"
    }
}