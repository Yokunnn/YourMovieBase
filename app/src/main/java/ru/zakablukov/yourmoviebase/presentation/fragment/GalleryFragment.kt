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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.databinding.FragmentGalleryBinding
import ru.zakablukov.yourmoviebase.presentation.adapter.GalleryAdapter
import ru.zakablukov.yourmoviebase.presentation.viewmodel.GalleryViewModel

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val binding by viewBinding(FragmentGalleryBinding::bind)
    private val viewModel: GalleryViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGalleryAdapter()
        initRecyclerView()

        observeMoviesResult()
        observeMoviesLoadState()
    }

    private fun initGalleryAdapter() {
        galleryAdapter = GalleryAdapter() { movie ->
            val bundle = Bundle().apply {
                putInt(ID, movie.id)
            }
            findNavController().navigate(
                R.id.action_galleryFragment_to_movieDetailsFragment, bundle
            )
        }
    }

    private fun initRecyclerView() {
        with(binding.galleryRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = galleryAdapter

            viewModel.getMovies(1, 10)
        }
    }

    private fun observeMoviesResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.moviesResult.collect { movies ->
                        movies?.let {
                            galleryAdapter?.update(it.toMutableList())
                        }
                    }
                }
            }
        }
    }

    private fun observeMoviesLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.moviesLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(LOAD_TAG, "loading")
                            LoadState.SUCCESS -> {
                                Log.d(LOAD_TAG, "success")
                                Toast.makeText(
                                    context, "Movies successfully loaded", Toast.LENGTH_SHORT
                                ).show()
                            }

                            LoadState.ERROR -> {
                                Log.d(LOAD_TAG, "error")
                                Toast.makeText(
                                    context, "Error while loading", Toast.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(LOAD_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val ID = "id"
        private const val LOAD_TAG = "Movies loading"
    }
}