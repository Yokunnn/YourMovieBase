package ru.zakablukov.yourmoviebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GenresRepositoryImpl
import ru.zakablukov.yourmoviebase.data.service.GenresService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenresModule {

    @Singleton
    @Provides
    fun provideGenresRepository(genresService: GenresService) =
        GenresRepositoryImpl(genresService)
}