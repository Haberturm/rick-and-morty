package com.haberturm.rickandmorty.presentation.screens.episodes

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.databinding.FragmentEpisodesFilterBinding
import com.haberturm.rickandmorty.databinding.FragmentLocationsFilterBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.FiltersMethods
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EpisodesFilterFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: EpisodesMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[EpisodesMainViewModel::class.java]
    }

    private val filtersMethods = FiltersMethods()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEpisodesFilterBinding.inflate(inflater)
        filtersMethods.setUpFilterEditText(
            editText = binding.nameEdit,
            onTextChanged = fun(text: CharSequence?) {
                viewModel.nameTextChanger(text)
            },
            textState = viewModel.nameText,
            lifecycleOwner = viewLifecycleOwner
        )

        filtersMethods.setUpFilterEditText(
            editText = binding.episodeEdit,
            onTextChanged = fun(text: CharSequence?) {
                viewModel.episodesTextChanger(text)
            },
            textState = viewModel.episodesText,
            lifecycleOwner = viewLifecycleOwner
        )

        binding.closeFiltersButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.applyFiltersButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            viewModel.getFilteredData()
        }

        binding.clearFiltersButton.setOnClickListener {
            viewModel.clearFilters()
        }

        return binding.root
    }
}

