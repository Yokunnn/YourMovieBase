package ru.zakablukov.yourmoviebase.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GalleryRepositoryImpl
import ru.zakablukov.yourmoviebase.data.service.GalleryService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {

    @Singleton
    @Provides
    fun provideGalleryService(retrofit: Retrofit): GalleryService =
        retrofit.create(GalleryService::class.java)

    @Singleton
    @Provides
    fun provideGalleryRepository(galleryService: GalleryService) =
        GalleryRepositoryImpl(galleryService)
}