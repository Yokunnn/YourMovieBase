package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentFilterBottomSheetBinding
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.domain.model.Genre
import ru.zakablukov.yourmoviebase.domain.model.TranslateText
import ru.zakablukov.yourmoviebase.presentation.adapter.GenreChipAdapter
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.presentation.viewmodel.GalleryViewModel

@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentFilterBottomSheetBinding::bind)
    private val galleryViewModel by navGraphViewModels<GalleryViewModel>(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }
    private var genreChipAdapter: GenreChipAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreChipAdapter = GenreChipAdapter()
        binding.genreRecyclerView.adapter = genreChipAdapter

        galleryViewModel.getAllLocalGenres()
        initBottomSheet()
        initResetButton()
        initApplyButton()

        observeFilterData()
        observeGenresLocalLoadState()
        observeGenresApiLoadState()
        observeGenresResult()
        observeTranslatedListLoadState()
        observeTranslatedListResult()
    }

    private fun initBottomSheet() {
        requireDialog().setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.isHideable = false
            val bottomSheetParent = binding.bottomSheetParent
            BottomSheetBehavior.from(bottomSheetParent.parent as View).peekHeight =
                bottomSheetParent.height
            bottomSheetBehavior.peekHeight = bottomSheetParent.height
            bottomSheetParent.parent.requestLayout()
        }
    }

    private fun initResetButton() {
        binding.resetButton.setOnClickListener {
            galleryViewModel.resetFilters()
            dismiss()
        }
    }

    private fun initApplyButton() {
        with(binding) {
            applyButton.setOnClickListener {
                if (galleryViewModel.isTextValid(yearEditText.text, lengthEditText.text)) {
                    val rating = galleryViewModel.reformatRatingString(ratingRangeSlider.values.toString())
                    val year = yearEditText.text
                    val length = lengthEditText.text
                    galleryViewModel.applyFilters(
                        FilterData(
                            genres = genreChipAdapter?.getCheckedGenres(),
                            rating = rating,
                            year = if (year.isNullOrBlank()) null else year.toString(),
                            length = if (length.isNullOrBlank()) null else length.toString()
                        )
                    )
                    dismiss()
                } else {
                    Snackbar.make(
                        binding.root,
                        resources.getString(R.string.snack_invalid_string_format),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeFilterData() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.filterData.collect { filterData ->
                        with(binding) {
                            filterData.genres?.let { checkedGenres ->
                                genreChipAdapter?.insertCheckedGenres(checkedGenres)
                            }
                            filterData.rating?.let {
                                ratingRangeSlider.values = galleryViewModel.reformatRatingToFloatValues(it)
                            }
                            filterData.year?.let {
                                yearEditText.setText(it)
                            }
                            filterData.length?.let {
                                lengthEditText.setText(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeGenresLocalLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.genresLocalLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(GENRES_LOCAL_LOAD_TAG, "loading")
                            LoadState.SUCCESS -> Log.d(GENRES_LOCAL_LOAD_TAG, "success")
                            LoadState.ERROR -> {
                                Log.d(GENRES_LOCAL_LOAD_TAG, "error")
                                Snackbar.make(
                                    binding.root,
                                    resources.getString(R.string.snack_error_cached_genres),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(GENRES_LOCAL_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeGenresApiLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.genresApiLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(GENRES_API_LOAD_TAG, "loading")
                            LoadState.SUCCESS -> Log.d(GENRES_API_LOAD_TAG, "success")
                            LoadState.ERROR -> {
                                Log.d(GENRES_API_LOAD_TAG, "error")
                                Snackbar.make(
                                    binding.root,
                                    resources.getString(R.string.snack_error_load_genres),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(GENRES_API_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeGenresResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.genresResult.collect { genres ->
                        if (genres.isNotEmpty()) {
                            genreChipAdapter?.update(genres.toMutableList())
                            galleryViewModel.tryTextLocalization(*genres.map {
                                TranslateText(
                                    GENRE, it.name
                                )
                            }.toTypedArray())
                        }
                    }
                }
            }
        }
    }

    private fun observeTranslatedListResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.translatedListResult.collect { translatedList ->
                        translatedList?.let { list ->
                            val genres = list.filter { it.type == GENRE }.map { Genre(it.text.lowercase()) }
                            genreChipAdapter?.updateEN(genres.toMutableList())
                        }
                    }
                }
            }
        }
    }

    private fun observeTranslatedListLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.translatedListLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(TRANSLATION_TAG, "loading")
                            LoadState.SUCCESS -> Log.d(TRANSLATION_TAG, "success")
                            LoadState.ERROR -> Log.d(TRANSLATION_TAG, "error")
                            null -> Log.d(TRANSLATION_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val GENRES_LOCAL_LOAD_TAG = "Local genres filters"
        private const val GENRES_API_LOAD_TAG = "Api genres filters"
        private const val TRANSLATION_TAG = "Text list translation"
        private const val GENRE = "Genre"
    }
}