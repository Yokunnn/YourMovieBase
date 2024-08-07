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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentFavouritesBinding
import ru.zakablukov.yourmoviebase.presentation.adapter.GalleryAdapter
import ru.zakablukov.yourmoviebase.presentation.viewmodel.FavouritesViewModel

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private val binding by viewBinding(FragmentFavouritesBinding::bind)
    private val viewModel: FavouritesViewModel by viewModels()
    private var galleryAdapter: GalleryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGalleryAdapter()
        initRecyclerView()

        observeMoviesResult()
    }

    private fun initGalleryAdapter() {
        galleryAdapter = GalleryAdapter() { movie ->
            val bundle = Bundle().apply {
                movie?.let { putInt(ID, it.id) }
            }
            findNavController().navigate(
                R.id.action_favouritesFragment_to_movieDetailsFragment, bundle
            )
        }.apply {
            addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is androidx.paging.LoadState.Error -> {
                        Log.d(LOAD_TAG, "error")
                        Snackbar.make(
                            binding.root,
                            resources.getString(R.string.snack_error_while_loading),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    androidx.paging.LoadState.Loading -> Log.d(LOAD_TAG, "loading")
                    is androidx.paging.LoadState.NotLoading -> Log.d(LOAD_TAG, "not loading")
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.favouritesRecyclerView) {
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
                            binding.favouritesProgressIndicator.visibility = View.GONE
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