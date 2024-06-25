package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import ru.zakablukov.yourmoviebase.databinding.FragmentGalleryBinding

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTestBtn()
    }

    private fun initTestBtn() {
        binding.testBtn.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().popBackStack()
        }
    }
}