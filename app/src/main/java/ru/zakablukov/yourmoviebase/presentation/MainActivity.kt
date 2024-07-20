package ru.zakablukov.yourmoviebase.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.ActivityMainBinding
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.presentation.viewmodel.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.loadOrRefreshGenres()
        setupBottomNavigation()

        observeGenresLoadStates()
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.galleryFragment || destination.id == R.id.favouritesFragment) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun observeGenresLoadStates() {
        observeApiGenresLoadState()
        observeGenresUpsertLoadState()
    }

    private fun observeGenresUpsertLoadState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genresUpsertLoadState.collect { loadState ->
                    when (loadState) {
                        LoadState.LOADING -> Log.d(UPSERT_LOAD_TAG, "loading")
                        LoadState.SUCCESS -> Log.d(UPSERT_LOAD_TAG, "success")
                        LoadState.ERROR -> {
                            Log.d(UPSERT_LOAD_TAG, "error")
                            Toast.makeText(
                                applicationContext,
                                "Error while upsert",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        null -> Log.d(UPSERT_LOAD_TAG, "init")
                    }
                }
            }
        }
    }

    private fun observeApiGenresLoadState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apiGenresLoadState.collect { loadState ->
                    when (loadState) {
                        LoadState.LOADING -> Log.d(API_LOAD_TAG, "loading")
                        LoadState.SUCCESS -> Log.d(API_LOAD_TAG, "success")
                        LoadState.ERROR -> {
                            Log.d(API_LOAD_TAG, "error")
                            Toast.makeText(
                                applicationContext,
                                "Error while loading genres",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        null -> Log.d(API_LOAD_TAG, "init")
                    }
                }
            }
        }
    }

    companion object {
        private const val API_LOAD_TAG = "Genres from API"
        private const val UPSERT_LOAD_TAG = "Genres upsert to db"
    }
}