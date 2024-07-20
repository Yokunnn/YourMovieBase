package ru.zakablukov.yourmoviebase.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.zakablukov.yourmoviebase.data.model.GenreResponse

interface GenresService {

    @GET(GET_ALL_GENRES_REQUEST)
    suspend fun getAllGenres(
        @Query(QUERY_FIELD) field: String = DEFAULT_FIELD
    ): List<GenreResponse>

    companion object {
        private const val GET_ALL_GENRES_REQUEST = "v1/movie/possible-values-by-field"
        private const val QUERY_FIELD = "field"
        private const val DEFAULT_FIELD = "genres.name"
    }
}