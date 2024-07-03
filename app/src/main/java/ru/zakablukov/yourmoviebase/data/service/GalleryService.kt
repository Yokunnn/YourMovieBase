package ru.zakablukov.yourmoviebase.data.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.zakablukov.yourmoviebase.data.model.MoviesResponse

interface GalleryService {

    @GET(GET_MOVIES_REQUEST)
    suspend fun getMovies(
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_LIMIT) limit: Int,
        @Query(QUERY_NOT_NULL_FIELDS) notNullFields: List<String> = DEFAULT_NOT_NULL_FIELDS,
        @Query(QUERY_SORT_FIELD) sortField: List<String> = DEFAULT_SORT_FIELD,
        @Query(QUERY_SORT_TYPE) sortType: List<String> = DEFAULT_SORT_TYPE,
        @Query(QUERY_TYPE) type: List<String> = DEFAULT_TYPE
    ): MoviesResponse

    companion object {
        private const val GET_MOVIES_REQUEST = "v1.4/movie"
        private const val QUERY_PAGE = "page"
        private const val QUERY_LIMIT = "limit"
        private const val QUERY_NOT_NULL_FIELDS = "notNullFields"
        private const val QUERY_SORT_FIELD = "sortField"
        private const val QUERY_SORT_TYPE = "sortType"
        private const val QUERY_TYPE = "type"

        private val DEFAULT_NOT_NULL_FIELDS = listOf(
            "alternativeName",
            "poster.url"
        )
        private val DEFAULT_SORT_FIELD = listOf(
            "rating.imdb",
            "rating.kp"
        )
        private val DEFAULT_SORT_TYPE = listOf(
            "-1",
            "-1"
        )
        private val DEFAULT_TYPE = listOf(
            "movie"
        )
    }
}