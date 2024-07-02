package ru.zakablukov.yourmoviebase.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.data.repositoryimpl.GalleryRepositoryImpl
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.Movie
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepositoryImpl: GalleryRepositoryImpl
) : ViewModel() {

    val moviesResult = MutableLiveData<List<Movie>>()
    val moviesLoadState = MutableLiveData<LoadState>()

    fun getMovies(page: Int, limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            galleryRepositoryImpl.getMovies(page, limit).collect { requestState ->
                when (requestState) {
                    is Request.Error -> moviesLoadState.postValue(LoadState.ERROR)
                    is Request.Loading -> moviesLoadState.postValue(LoadState.LOADING)
                    is Request.Success -> {
                        moviesLoadState.postValue(LoadState.SUCCESS)
                        moviesResult.postValue(requestState.data)
                    }
                }
            }
        }
    }
}