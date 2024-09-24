package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentMovieDetailsBinding
import ru.zakablukov.yourmoviebase.domain.model.TranslateText
import ru.zakablukov.yourmoviebase.presentation.adapter.PersonAdapterSmall
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.presentation.viewmodel.MovieDetailsViewModel

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val binding by viewBinding(FragmentMovieDetailsBinding::bind)
    private val viewModel: MovieDetailsViewModel by viewModels()
    private var personAdapter: PersonAdapterSmall? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        personAdapter = PersonAdapterSmall()
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActorsRecyclerView()
        requestForMovie()

        observeMovieDetailsResult()
        observeApiDetailsLoadState()
        observeLocalDetailsLoadState()
        observeMovieUpsertLocalLoadState()
        observeIsFavouriteLoadState()
        observeIsFavouriteResult()
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
                                    if (!viewModel.tryTextLocalization(TranslateText(GENRE, genre.name))) {
                                        genreChipGroup.addView(createChip(genre.name))
                                    }
                                }
                                viewModel.tryTextLocalization(TranslateText(DESCRIPTION, it.description))
                                personAdapter?.update(it.persons.toMutableList())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeApiDetailsLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.movieApiDetailsLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(API_LOAD_TAG, "loading")
                            LoadState.SUCCESS -> {
                                Log.d(API_LOAD_TAG, "success")
                                initLikeButton()
                                Snackbar.make(
                                    binding.root,
                                    resources.getString(R.string.snack_details_load_success),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            LoadState.ERROR -> {
                                Log.d(API_LOAD_TAG, "error")
                                Snackbar.make(
                                    binding.root,
                                    resources.getString(R.string.snack_details_load_error),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(API_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeLocalDetailsLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.movieLocalDetailsLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(LOCAL_LOAD_TAG, "loading")
                            LoadState.SUCCESS -> {
                                Log.d(LOCAL_LOAD_TAG, "success")
                                Snackbar.make(
                                    binding.root,
                                    resources.getString(R.string.snack_details_load_success),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            LoadState.ERROR -> {
                                Log.d(LOCAL_LOAD_TAG, "error")
                                Snackbar.make(
                                    binding.root,
                                    resources.getString(R.string.snack_details_load_error),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(LOCAL_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeMovieUpsertLocalLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.movieUpsertLocalLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(UPSERT_LOCAL_LOAD_TAG, "loading")
                            LoadState.SUCCESS -> Log.d(UPSERT_LOCAL_LOAD_TAG, "success")
                            LoadState.ERROR -> Log.d(UPSERT_LOCAL_LOAD_TAG, "error")
                            null -> Log.d(UPSERT_LOCAL_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeIsFavouriteLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isFavouriteLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(IS_FAVOURITE_LOAD_TAG, "loading")
                            LoadState.SUCCESS -> Log.d(IS_FAVOURITE_LOAD_TAG, "success")
                            LoadState.ERROR -> Log.d(IS_FAVOURITE_LOAD_TAG, "error")
                            null -> Log.d(IS_FAVOURITE_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeIsFavouriteResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isFavouriteResult.collect { isFavourite ->
                        when (isFavourite) {
                            true -> {
                                Log.d(IS_FAVOURITE_RES_LOAD_TAG, "favourite")
                                initDislikeButton()
                            }

                            false -> {
                                Log.d(IS_FAVOURITE_RES_LOAD_TAG, "not favourite")
                                initLikeButton()
                            }

                            null -> Log.d(IS_FAVOURITE_RES_LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun initLikeButton() {
        with(binding.likeButton) {
            visibility = View.VISIBLE
            setIconResource(R.drawable.icon_not_favourite)
            setIconTintResource(R.color.yellow)
            setOnClickListener {
                viewModel.updateIsFavouriteById(true)
            }
        }
    }

    private fun initDislikeButton() {
        with(binding.likeButton) {
            visibility = View.VISIBLE
            setIconResource(R.drawable.icon_favourite)
            setIconTintResource(R.color.red)
            setOnClickListener {
                viewModel.updateIsFavouriteById(false)
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
        private const val API_LOAD_TAG = "Details loaded from api"
        private const val LOCAL_LOAD_TAG = "Details loaded from local"
        private const val UPSERT_LOCAL_LOAD_TAG = "Movie upsert local"
        private const val IS_FAVOURITE_LOAD_TAG = "Movie check favourite"
        private const val IS_FAVOURITE_RES_LOAD_TAG = "Movie favourite res"
        private const val TRANSLATION_TAG = "Text translation"
        private const val DESCRIPTION = "Description"
        private const val GENRE = "Genre"
    }
}