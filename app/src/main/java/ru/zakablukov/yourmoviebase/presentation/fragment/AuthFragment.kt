package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentAuthBinding
import ru.zakablukov.yourmoviebase.presentation.enums.LoadState
import ru.zakablukov.yourmoviebase.presentation.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var googleCredentialRequest: GetCredentialRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel.getCurrentUser()
        observeCurrentUser()

        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoginBtn()
        initRegisterBtn()
        initGoogleSignInBtn()

        observeSignInLoadState()
        observeRegisterLoadState()
        observeGoogleSignInLoadState()
    }

    private fun initLoginBtn() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.signInWithEmailAndPassword(email, password)
        }
    }

    private fun initRegisterBtn() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.createUserWithEmailAndPassword(email, password)
        }
    }

    private fun initGoogleSignInBtn() {
        binding.googleSignInButton.setOnClickListener {
            viewModel.tryGetCredentials(requireContext())
        }
    }

    private fun observeCurrentUser() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userResult.collect { user ->
                        user?.let {
                            viewModel.requestEmailVerification()
                            findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
                        }
                    }
                }
            }
        }
    }

    private fun observeSignInLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.signInLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> {
                                Log.d(SIGN_IN_TAG, "loading")
                                binding.loginProgressIndicator.visibility = View.VISIBLE
                                binding.loginButton.textScaleX = 0f
                            }
                            LoadState.SUCCESS -> {
                                Log.d(SIGN_IN_TAG, "user successfully signed in")
                                viewModel.requestEmailVerification()
                                findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
                            }

                            LoadState.ERROR -> {
                                Log.d(SIGN_IN_TAG, "user auth failed")
                                binding.loginProgressIndicator.visibility = View.GONE
                                binding.loginButton.textScaleX = 1f
                                Toast.makeText(
                                    context, "User authentication failed", Toast.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(SIGN_IN_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeRegisterLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.registerLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> {
                                Log.d(REGISTER_TAG, "loading")
                                binding.registerProgressIndicator.visibility = View.VISIBLE
                                binding.registerButton.textScaleX = 0f
                            }
                            LoadState.SUCCESS -> {
                                Log.d(REGISTER_TAG, "user successfully registered")
                                viewModel.requestEmailVerification()
                                findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
                            }

                            LoadState.ERROR -> {
                                Log.d(REGISTER_TAG, "user registration failed")
                                binding.registerProgressIndicator.visibility = View.GONE
                                binding.registerButton.textScaleX = 1f
                                Toast.makeText(
                                    context, "User registration failed", Toast.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(REGISTER_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    private fun observeGoogleSignInLoadState() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.signInGoogleLoadState.collect { loadState ->
                        when (loadState) {
                            LoadState.LOADING -> Log.d(SIGN_IN_GOOGLE_TAG, "loading")
                            LoadState.SUCCESS -> {
                                Log.d(SIGN_IN_GOOGLE_TAG, "user successfully signed in with google")
                                viewModel.requestEmailVerification()
                                findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
                            }

                            LoadState.ERROR -> {
                                Log.d(SIGN_IN_GOOGLE_TAG, "user google auth failed")
                                Toast.makeText(
                                    context, "User google authentication failed", Toast.LENGTH_SHORT
                                ).show()
                            }

                            null -> Log.d(SIGN_IN_GOOGLE_TAG, "init")
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val SIGN_IN_TAG = "Firebase sign in"
        private const val REGISTER_TAG = "Firebase registration"
        private const val SIGN_IN_GOOGLE_TAG = "Firebase Google sign in"
    }
}