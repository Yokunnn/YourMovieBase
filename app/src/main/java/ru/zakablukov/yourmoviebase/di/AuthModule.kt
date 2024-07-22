package ru.zakablukov.yourmoviebase.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.zakablukov.yourmoviebase.BuildConfig
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

    @Singleton
    @Provides
    fun provideGoogleIdOption() = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce("generating a Google ID token")
        .build()

    @Singleton
    @Provides
    fun provideGoogleCredentialRequest(getGoogleIdOption: GetGoogleIdOption) =
        GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleIdOption)
            .build()

    @Singleton
    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context) =
        CredentialManager.create(context)
}