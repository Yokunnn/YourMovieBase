package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentAuthBinding

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val binding by viewBinding(FragmentAuthBinding::bind)
    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        auth?.currentUser?.let {
            findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
        }

        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoginBtn()
        initRegisterBtn()
    }

    private fun initLoginBtn() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase auth", "user successfully signed in")
                        findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
                    } else {
                        Log.d("Firebase auth", "user auth failed")
                        Toast.makeText(
                            context,
                            "User authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun initRegisterBtn() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase register", "user successfully registered")
                        findNavController().navigate(R.id.action_authFragment_to_galleryFragment)
                    } else {
                        Log.d("Firebase register", "user registration failed")
                        Toast.makeText(
                            context,
                            "User registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}