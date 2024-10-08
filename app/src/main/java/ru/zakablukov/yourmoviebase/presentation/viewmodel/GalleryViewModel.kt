package ru.zakablukov.yourmoviebase.presentation.viewmodel

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.DatabaseRepositoryImpl
import ru.zakablukov.yourmoviebase.data.repositoryimpl.MovieRepositoryImpl
import ru.zakablukov.yourmoviebase.data.repositoryimpl.TranslateRepositoryImpl
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.model.Movie
import ru.zakablukov.yourmoviebase.domain.model.TranslateText
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val movieRepositoryImpl: MovieRepositoryImpl,
    private val translateRepositoryImpl: TranslateRepositoryImpl,
    private val databaseRepositoryImpl: DatabaseRepositoryImpl,
) : ViewModel(), LocalizedViewModel {

    private val _filterData = MutableStateFlow(FilterData())
    val filterData: StateFlow<FilterData> = _filterData
    private val _moviesResult = MutableStateFlow<PagingData<Movie>?>(PagingData.empty())
    val moviesResult: StateFlow<PagingData<Movie>?> = _moviesResult

    private val _genresLocalLoadState = MutableStateFlow<LoadState?>(null)
    val genresLocalLoadState: StateFlow<LoadState?> = _genresLocalLoadState
    private val _genresApiLoadState = MutableStateFlow<LoadState?>(null)
    val genresApiLoadState: StateFlow<LoadState?> = _genresApiLoadState
    private val _genresResult = MutableStateFlow<List<Genre>>(emptyList())
    val genresResult: StateFlow<List<Genre>> = _genresResult

    private val _translatedListResult = MutableStateFlow<List<TranslateText>?>(null)
    val translatedListResult: StateFlow<List<TranslateText>?> = _translatedListResult
    private val _translatedListLoadState = MutableStateFlow<LoadState?>(null)
    val translatedListLoadState: StateFlow<LoadState?> = _translatedListLoadState

    fun applyFilters(filterData: FilterData) {
        viewModelScope.launch(Dispatchers.IO) {
            _filterData.emit(filterData)
        }
    }

    fun resetFilters() {
        viewModelScope.launch(Dispatchers.IO) {
            _filterData.emit(FilterData())
        }
    }

    suspend fun refreshMovies() {
        _moviesResult.emit(getPagingData())
    }

    private suspend fun getPagingData(): PagingData<Movie>? {
        return movieRepositoryImpl.getMovies(_filterData.value)
            .cachedIn(viewModelScope).firstOrNull()
    }

    fun getAllLocalGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepositoryImpl.getAllGenres().collect { requestState ->
                when (requestState) {
                    is Request.Error -> {
                        _genresLocalLoadState.emit(LoadState.ERROR)
                        getAndSaveGenres()
                    }

                    is Request.Loading -> _genresLocalLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _genresLocalLoadState.emit(LoadState.SUCCESS)
                        _genresResult.emit(requestState.data)
                    }
                }
            }
        }
    }

    private suspend fun getAndSaveGenres() {
        movieRepositoryImpl.getAllGenres().collect { requestState ->
            when (requestState) {
                is Request.Error -> _genresApiLoadState.emit(LoadState.ERROR)
                is Request.Loading -> _genresApiLoadState.emit(LoadState.LOADING)
                is Request.Success -> {
                    _genresApiLoadState.emit(LoadState.SUCCESS)
                    _genresResult.emit(requestState.data)
                    databaseRepositoryImpl.upsertAllGenres(requestState.data)
                }
            }
        }
    }

    private fun translateListRUtoEN(list: List<TranslateText>) {
        viewModelScope.launch(Dispatchers.IO) {
            translateRepositoryImpl.translateListRUtoEN(list).collect { requestState ->
                when (requestState) {
                    is Request.Error -> _translatedListLoadState.emit(LoadState.ERROR)
                    is Request.Loading -> _translatedListLoadState.emit(LoadState.LOADING)
                    is Request.Success -> {
                        _translatedListLoadState.emit(LoadState.SUCCESS)
                        _translatedListResult.emit(requestState.data)
                    }
                }
            }
        }
    }

    fun isTextValid(year: Editable?, length: Editable?): Boolean =
        (REGEX.matches(year.toString()) || year.isNullOrBlank()) &&
                (REGEX.matches(length.toString()) || length.isNullOrBlank())

    fun reformatRatingString(rating: String) = rating
        .replace(REGEX_RATING_FIX_STRING, "")
        .replace(", ", "-")

    fun reformatRatingToFloatValues(rating: String) =
        REGEX_RATING.findAll(rating).toList().map {
            it.value.toFloat()
        }

    override fun tryTextLocalization(vararg translateTexts: TranslateText): Boolean {
        if (Locale.getDefault().language != "ru") {
            translateListRUtoEN(translateTexts.toList())
            return true
        } else {
            return false
        }
    }

    companion object {
        private val REGEX = Regex("\\d+|\\d+-\\d+")
        private val REGEX_RATING = Regex("\\d+\\.\\d")
        private val REGEX_RATING_FIX_STRING = Regex("\\[|\\]")
    }
}