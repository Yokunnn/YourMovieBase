package ru.zakablukov.yourmoviebase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zakablukov.yourmoviebase.data.repositoryimpl.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth) =
        AuthRepositoryImpl(firebaseAuth)

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth
}