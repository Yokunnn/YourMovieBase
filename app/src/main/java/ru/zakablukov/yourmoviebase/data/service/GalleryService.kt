package ru.zakablukov.yourmoviebase.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.zakablukov.yourmoviebase.data.model.MoviesResponse

interface GalleryService {

    @GET("v1.4/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("notNullFields") notNullFields: List<String> = listOf(
            "enName",
            "poster.url"
        ),
        @Query("sortField") sortField: List<String> = listOf(
            "rating.imdb"
        ),
        @Query("sortType") sortType: List<String> = listOf(
            "-1"
        )
    ): MoviesResponse
}