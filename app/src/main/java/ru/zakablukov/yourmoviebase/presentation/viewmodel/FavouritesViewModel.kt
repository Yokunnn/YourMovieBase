package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.zakablukov.yourmoviebase.data.repositoryimpl.DatabaseRepositoryImpl
import ru.zakablukov.yourmoviebase.domain.model.Movie
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val databaseRepositoryImpl: DatabaseRepositoryImpl
) : ViewModel(){

    val moviesResult: StateFlow<PagingData<Movie>?> =
        databaseRepositoryImpl.getFavouriteMovies()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
}