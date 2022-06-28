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
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CharactersFilterFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CharactersMainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharactersFilterBinding.inflate(inflater)

        setUpDropDownMenu(
            menu = (binding.genderMenu.editText as AutoCompleteTextView),
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.filter_dropdown_item,
                viewModel.genderItems
            ),
            onItemSelected = fun(position: Int){
                viewModel.genderPositionChanger(position)
            },
            selectedPosition = viewModel.genderPosition.value!!
        )

        setUpDropDownMenu(
            menu = (binding.statusMenu.editText as AutoCompleteTextView),
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.filter_dropdown_item,
                viewModel.statusItems
            ),
            onItemSelected = fun(position: Int){
                viewModel.statusPositionChanger(position)

            },
            selectedPosition = viewModel.statusPosition.value!!
        )

        setUpFilterEditText(
            editText = binding.nameEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.nameTextChanger(text)
            },
            textState = viewModel.nameText,
            lifecycleOwner = viewLifecycleOwner
        )

        setUpFilterEditText(
            editText = binding.speciesEdit,
            onTextChanged = fun(text: CharSequence?){
                viewModel.speciesTextChanger(text)
            },
            textState = viewModel.speciesText,
            lifecycleOwner = viewLifecycleOwner
        )

        setUpFilterEditText(
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

        return binding.root
    }
}

fun setUpDropDownMenu(
    menu: AutoCompleteTextView,
    adapter: ArrayAdapter<String>,
    onItemSelected: (Int) -> Unit,
    selectedPosition: Int
){
    menu.apply {
        setAdapter(adapter)
        setText(adapter.getItem(selectedPosition).toString(), false)
        setOnItemClickListener { _, _, position, _ ->
            onItemSelected(position)
        }
    }
}

fun setUpFilterEditText(
    editText: EditText,
    onTextChanged: (CharSequence?) -> Unit,
    textState: LiveData<String>,
    lifecycleOwner: LifecycleOwner
){
    val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged(p0)
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    editText.addTextChangedListener(textWatcher)

    textState.observe(lifecycleOwner){ text->
        editText.apply {
            applyWithDisabledTextWatcher(textWatcher){
                setText(text)
                setSelection(length())
            }
        }
    }
}



fun TextView.applyWithDisabledTextWatcher(textWatcher: TextWatcher, codeBlock: TextView.() -> Unit) {
    this.removeTextChangedListener(textWatcher)
    codeBlock()
    this.addTextChangedListener(textWatcher)
}