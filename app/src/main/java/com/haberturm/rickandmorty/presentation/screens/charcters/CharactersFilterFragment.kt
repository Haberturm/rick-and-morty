package com.haberturm.rickandmorty.presentation.screens.charcters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
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
            onItemSelected = fun(text: String){
                viewModel.genderTextChanger(text)
            }
        )

        setUpDropDownMenu(
            menu = (binding.statusMenu.editText as AutoCompleteTextView),
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.filter_dropdown_item,
                viewModel.statusItems
            ),
            onItemSelected = fun(text: String){
                viewModel.statusTextChanger(text)

            }
        )

        binding.nameSearchView.setQuery(viewModel.nameText.value, false)
        setSearchViewTextListener(
            searchView = binding.nameSearchView,
            action = fun(text: String?){
                viewModel.nameTextChanger(text)
            }
        )

        setSearchViewTextListener(
            searchView = binding.speciesSearchView,
            action = fun(text: String?){
                viewModel.speciesTextChanger(text)
            }
        )

        setSearchViewTextListener(
            searchView = binding.typeSearchView,
            action = fun(text: String?){
                viewModel.typeTextChanger(text)
            }
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

    fun setSearchViewTextListener(
        searchView: SearchView,
        action: (String?) -> Unit
    ){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                action(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                action(newText)
                return true
            }
        })
    }

    fun <T>setUpDropDownMenu(
        menu: AutoCompleteTextView,
        adapter: ArrayAdapter<T>,
        onItemSelected: (String) -> Unit
    ){
        menu.apply {
            setAdapter(adapter)
            setText(adapter.getItem(0).toString(), false)
            setOnItemClickListener { _, _, position, _ ->
                val item = adapter.getItem(position).toString()
                onItemSelected(item)
            }
        }
    }
}