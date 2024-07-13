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

        initFilterButton()
        initGalleryAdapter()
        initRecyclerView()

        observeMoviesResult()
    }

    private fun initFilterButton() {
        binding.filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_galleryFragment_to_filterBottomSheetFragment)
        }
    }

    private fun initGalleryAdapter() {
        galleryAdapter = GalleryAdapter() { movie ->
            val bundle = Bundle().apply {
                movie?.let { putInt(ID, it.id) }
            }
            findNavController().navigate(
                R.id.action_galleryFragment_to_movieDetailsFragment, bundle
            )
        }.apply {
            addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is androidx.paging.LoadState.Error -> {
                        Log.d(LOAD_TAG, "error")
                        Toast.makeText(
                            context, "Error while loading", Toast.LENGTH_SHORT
                        ).show()
                    }
                    androidx.paging.LoadState.Loading -> Log.d(LOAD_TAG, "loading")
                    is androidx.paging.LoadState.NotLoading -> Log.d(LOAD_TAG, "not loading")
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.galleryRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = galleryAdapter
        }
    }

    private fun observeMoviesResult() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.moviesResult.collect { movies ->
                        movies?.let {
                            galleryAdapter?.submitData(movies)
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