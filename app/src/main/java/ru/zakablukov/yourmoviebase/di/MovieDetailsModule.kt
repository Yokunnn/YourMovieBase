package ru.zakablukov.yourmoviebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zakablukov.yourmoviebase.data.repositoryimpl.MovieDetailsRepositoryImpl
import ru.zakablukov.yourmoviebase.data.service.MovieDetailsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailsModule {

    @Singleton
    @Provides
    fun provideMovieDetailsRepository(movieDetailsService: MovieDetailsService) =
        MovieDetailsRepositoryImpl(movieDetailsService)
}