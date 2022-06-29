package com.haberturm.rickandmorty.presentation.screens.charcters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharactersFilterBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.FiltersMethods
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CharactersFilterFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CharactersMainViewModel::class.java]
    }

    private val filtersMethods = FiltersMethods()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharactersFilterBinding.inflate(inflater)

        filtersMethods.setUpDropDownMenu(
            menu = (binding.genderMenu.editText as AutoCompleteTextView),
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.filter_dropdown_item,
                viewModel.genderItems
            ),
            onItemSelected = fun(position: Int){
                viewModel.genderPositionChanger(position)
            },
            selectedPositionState = viewModel.genderPosition,
            lifecycleOwner = viewLifecycleOwner
        )

        filtersMethods.setUpDropDownMenu(
            menu = (binding.statusMenu.editText as AutoCompleteTextView),
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.filter_dropdown_item,
                viewModel.statusItems
            ),
            onItemSelected = fun(position: Int){
                viewModel.statusPositionChanger(position)

            },
            selectedPositionState = viewModel.statusPosition,
            lifecycleOwner = viewLifecycleOwner
        )

        filtersMethods.setUpFilterEditText(
            editText = binding.nameEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.nameTextChanger(text)
            },
            textState = viewModel.nameText,
            lifecycleOwner = viewLifecycleOwner
        )

        filtersMethods.setUpFilterEditText(
            editText = binding.speciesEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.speciesTextChanger(text)
            },
            textState = viewModel.speciesText,
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


