package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentMovieDetailsBinding
import ru.zakablukov.yourmoviebase.domain.model.TranslateText
import ru.zakablukov.yourmoviebase.presentation.adapter.PersonAdapterSmall
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.presentation.viewmodel.MovieDetailsViewModel
import java.util.Locale

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val viewModel: MovieDetailsViewModel by viewModels()
    private var personAdapter: PersonAdapterSmall? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personAdapter = PersonAdapterSmall()
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActorsRecyclerView()
        requestForMovie()

        observeMovieDetailsResult()
        observeMovieDetailsLoadState()
        observeTranslatedTextResult()
        observeTranslatedTextLoadState()
    }

    private fun initActorsRecyclerView() {
        with(binding.actorsRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = personAdapter
        }
    }

    private fun requestForMovie() {
        if (requireArguments().containsKey(ID)) {
            val id = requireArguments().getInt(ID)
            viewModel.getMovieById(id)
        }
    }

    private fun observeMovieDetailsResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.movieDetailsResult.collect { movie ->
                        with(binding) {
                            movieNameTextView.text = movie?.name
                            yearValueTextView.text = movie?.year.toString()
                            descriptionTextView.text = movie?.description
                            lengthValueTextView.text = requireContext().getString(
                                R.string.util_length,
                                movie?.length?.div(60),
                                movie?.length?.mod(60)
                            )
                            ageRatingValueTextView.text = requireContext().getString(
                                R.string.util_age_rating,
                                movie?.ageRating
                            )
                            ratingValueTextView.text = requireContext().getString(
                                R.string.util_rating,
                                movie?.rating
                            )
                            Glide.with(posterImageView.context).load(movie?.posterUrl)
                                .into(posterImageView)
                            movie?.let {
                                it.genres.forEach { genre ->
                                    if (Locale.getDefault().language != "ru") {
                                        viewModel.translateRUtoEN(TranslateText(GENRE, genre))
                                    } else {
                                        genreChipGroup.addView(createChip(genre))
                                    }
                                }
                                if (Locale.getDefault().language != "ru") {
                                    viewModel.translateRUtoEN(
                                        TranslateText(
                                            DESCRIPTION,
                                            it.description
                                        )
                                    )
                                }
                                personAdapter?.update(it.persons.toMutableList())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeMovieDetailsLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.movieDetailsLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(LOAD_TAG, "loading")
                            LoadState.SUCCESS -> {
                                Log.d(LOAD_TAG, "success")
                                Toast.makeText(
                                    context,
                                    "Details successfully loaded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            LoadState.ERROR -> {
                                Log.d(LOAD_TAG, "error")
                                Toast.makeText(
                                    context,
                                    "Error while loading details",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeTranslatedTextResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.translatedTextResult.collect { translateText ->
                        when (translateText?.type) {
                            GENRE -> binding.genreChipGroup.addView(createChip(translateText.text.lowercase()))
                            DESCRIPTION -> binding.descriptionTextView.text = translateText.text
                        }
                    }
                }
            }
        }
    }

    private fun observeTranslatedTextLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.translatedTextLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(TRANSLATION_TAG, "translating")
                            LoadState.SUCCESS -> Log.d(TRANSLATION_TAG, "success translation")
                            LoadState.ERROR -> Log.d(TRANSLATION_TAG, "error translation")
                            null -> Log.d(TRANSLATION_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun createChip(info: String): Chip {
        val chip = Chip(context).apply {
            text = info
            isCheckable = false
            isClickable = false
        }
        return chip
    }

    companion object {
        private const val ID = "id"
        private const val LOAD_TAG = "Details loaded"
        private const val TRANSLATION_TAG = "Text translation"
        private const val DESCRIPTION = "Description"
        private const val GENRE = "Genre"
    }
}