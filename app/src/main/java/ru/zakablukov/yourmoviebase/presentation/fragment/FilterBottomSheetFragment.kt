package ru.zakablukov.yourmoviebase.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zakablukov.yourmoviebase.R
import ru.zakablukov.yourmoviebase.databinding.FragmentFilterBottomSheetBinding
import ru.zakablukov.yourmoviebase.domain.model.FilterData
import ru.zakablukov.yourmoviebase.presentation.viewmodel.GalleryViewModel

@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentFilterBottomSheetBinding::bind)
    private val galleryViewModel by navGraphViewModels<GalleryViewModel>(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheet()
        initResetButton()
        initApplyButton()

        observeFilterData()
    }

    private fun initBottomSheet() {
        requireDialog().setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.isHideable = false
            val bottomSheetParent = binding.bottomSheetParent
            BottomSheetBehavior.from(bottomSheetParent.parent as View).peekHeight =
                bottomSheetParent.height
            bottomSheetBehavior.peekHeight = bottomSheetParent.height
            bottomSheetParent.parent.requestLayout()
        }
    }

    private fun initResetButton() {
        binding.resetButton.setOnClickListener {
            galleryViewModel.resetFilters()
            dismiss()
        }
    }

    private fun initApplyButton() {
        with(binding) {
            applyButton.setOnClickListener {
                if (isTextValid()) {
                    val rating = ratingRangeSlider.values.toString()
                        .replace(REGEX_RATING_FIX_STRING, "")
                        .replace(", ", "-")
                    val year = yearEditText.text
                    val length = lengthEditText.text
                    galleryViewModel.applyFilters(
                        FilterData(
                            rating = rating,
                            year = if (year.isNullOrBlank()) null else year.toString(),
                            length = if (length.isNullOrBlank()) null else length.toString()
                        )
                    )
                    dismiss()
                } else {
                    Toast.makeText(
                        context,
                        "Invalid string format",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeFilterData() {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    galleryViewModel.filterData.collect { filterData ->
                        with(binding) {
                            filterData.rating?.let { rating ->
                                val values = REGEX_RATING.findAll(rating).toList().map {
                                    it.value.toFloat()
                                }
                                ratingRangeSlider.values = values
                            }
                            filterData.year?.let {
                                yearEditText.setText(it)
                            }
                            filterData.length?.let {
                                lengthEditText.setText(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isTextValid(): Boolean {
        with(binding) {
            val year = yearEditText.text
            val length = lengthEditText.text
            return (REGEX.matches(year.toString()) || year.isNullOrBlank()) &&
                    (REGEX.matches(length.toString()) || length.isNullOrBlank())
        }
    }

    companion object {
        private val REGEX = Regex("\\d+|\\d+-\\d+")
        private val REGEX_RATING = Regex("\\d+\\.\\d")
        private val REGEX_RATING_FIX_STRING = Regex("\\[|\\]")
    }
}