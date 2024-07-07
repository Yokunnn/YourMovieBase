package ru.zakablukov.yourmoviebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zakablukov.yourmoviebase.data.pagingsource.GalleryPagingSource
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GalleryRepositoryImpl
import ru.zakablukov.yourmoviebase.data.service.GalleryService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {

    @Singleton
    @Provides
    fun provideGalleryRepository(galleryPagingSource: GalleryPagingSource) =
        GalleryRepositoryImpl(galleryPagingSource)

    @Singleton
    @Provides
    fun provideGalleryPagingSource(galleryService: GalleryService) =
        GalleryPagingSource(galleryService)
}