package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryAdapter = GalleryAdapter()
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        observeMoviesResult()
        observeMoviesLoadState()
    }

    private fun initRecyclerView() {
        with(binding.galleryRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = galleryAdapter

            viewModel.getMovies(1, 10)
        }
    }

    private fun observeMoviesResult() {
        viewModel.moviesResult.observe(viewLifecycleOwner) { movies ->
            galleryAdapter?.update(movies.toMutableList())
        }
    }

    private fun observeMoviesLoadState() {
        viewModel.moviesLoadState.observe(viewLifecycleOwner) { loadState ->
            when (loadState) {
                LoadState.LOADING -> {
                    Log.d("Movies loading", "loading")
                }

                LoadState.SUCCESS -> {
                    Log.d("Movies loading", "success")
                    Toast.makeText(
                        context,
                        "Movies successfully loaded",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                LoadState.ERROR -> {
                    Log.d("Movies loading", "error")
                    Toast.makeText(
                        context,
                        "Error while loading",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}