package ru.zakablukov.yourmoviebase.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<Request<AuthResult>>

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Flow<Request<AuthResult>>

    suspend fun signInWithGoogle(credential: AuthCredential): Flow<Request<AuthResult>>

    suspend fun getCurrentUser(): Flow<Request<FirebaseUser?>>

    suspend fun signOut(): Flow<Request<Unit>>
}