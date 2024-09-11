package ru.zakablukov.yourmoviebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zakablukov.yourmoviebase.data.pagingsource.GalleryPagingSource
import ru.zakablukov.yourmoviebase.data.repositoryimpl.MovieRepositoryImpl
import ru.zakablukov.yourmoviebase.data.service.GalleryService
import ru.zakablukov.yourmoviebase.data.service.GenresService
import ru.zakablukov.yourmoviebase.data.service.MovieDetailsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        galleryPagingSource: GalleryPagingSource,
        movieDetailsService: MovieDetailsService,
        genresService: GenresService,
    ) = MovieRepositoryImpl(galleryPagingSource, movieDetailsService, genresService)

    @Singleton
    @Provides
    fun provideGalleryPagingSource(galleryService: GalleryService) =
        GalleryPagingSource(galleryService)
}