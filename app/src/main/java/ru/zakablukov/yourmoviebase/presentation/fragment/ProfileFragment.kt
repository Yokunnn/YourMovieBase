package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.presentation.compose.ProfileScreen
import ru.zakablukov.yourmoviebase.presentation.viewmodel.ProfileViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewmodel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme() {
                    ProfileScreen(
                        viewModel = viewmodel,
                        {
                            findNavController().navigate(R.id.action_profileFragment_to_authFragment)
                        }
                    )
                }
            }
        }
    }
}