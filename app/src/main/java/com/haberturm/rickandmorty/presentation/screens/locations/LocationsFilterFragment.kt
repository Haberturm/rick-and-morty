package com.haberturm.rickandmorty.presentation.screens.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharactersFilterBinding
import com.haberturm.rickandmorty.databinding.FragmentLocationsFilterBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.FiltersMethods
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class LocationsFilterFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LocationsMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[LocationsMainViewModel::class.java]
    }

    private val filtersMethods = FiltersMethods()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLocationsFilterBinding.inflate(inflater)

        filtersMethods.setUpFilterEditText(
            editText = binding.nameEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.nameTextChanger(text)
            },
            textState = viewModel.nameText,
            lifecycleOwner = viewLifecycleOwner
        )

        filtersMethods.setUpFilterEditText(
            editText = binding.dimensionEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.dimensionTextChanger(text)
            },
            textState = viewModel.dimensionText,
            lifecycleOwner = viewLifecycleOwner
        )

        filtersMethods.setUpFilterEditText(
            editText = binding.typeEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.typeTextChanger(text)
            },
            textState = viewModel.typeText,
            lifecycleOwner = viewLifecycleOwner
        )

        binding.closeFiltersButton.setOnClickListener {
            viewModel.closeFilters()
            parentFragmentManager.popBackStack()
        }

        binding.applyFiltersButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            viewModel.applyFilters()
        }

        binding.clearFiltersButton.setOnClickListener {
            viewModel.clearFilters()
        }

        return binding.root
    }


}