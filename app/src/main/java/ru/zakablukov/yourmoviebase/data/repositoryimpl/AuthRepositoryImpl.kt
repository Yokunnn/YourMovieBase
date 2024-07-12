package ru.zakablukov.yourmoviebase.data.repositoryimpl

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Request<AuthResult>> {
        return requestFlow {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Request<AuthResult>> {
        return requestFlow {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            authResult
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Flow<Request<AuthResult>> {
        return requestFlow {
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            authResult
        }
    }

    override suspend fun getCurrentUser(): Flow<Request<FirebaseUser?>> {
        return requestFlow {
            firebaseAuth.currentUser
        }
    }

    override suspend fun signOut(): Flow<Request<Unit>> {
        return requestFlow {
            firebaseAuth.signOut()
        }
    }
}