package ru.zakablukov.yourmoviebase.data.service

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.zakablukov.yourmoviebase.BuildConfig
import ru.zakablukov.yourmoviebase.data.model.MoviesResponse

interface GalleryService {

    @Headers("X-API-KEY: ${BuildConfig.X_API_KEY}")
    @GET("v1.4/movies")
    suspend fun getMovies(
        @Query("page") page: Int
    ): MoviesResponse
}