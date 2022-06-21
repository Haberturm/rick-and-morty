package com.haberturm.rickandmorty.presentation.charcters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharactersMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CharactersMainFragment : DaggerFragment() {
    private lateinit var charactersAdapter: CharacterListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CharactersMainViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charactersAdapter = CharacterListAdapter(
            listener = object : CharacterListAdapter.ActionClickListener{
                override fun showDetail(id: Int) {
                    viewModel.showDetails()
                }
            },
            context = requireContext()
        )
        viewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharactersMainBinding.inflate(inflater)
        binding.charactersList.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = charactersAdapter
            addItemDecoration(
                GridSpacingItemDecoration(2,resources.getDimensionPixelSize(R.dimen.small_margin) , true, 0)
            )
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
            if (state != null) {
                when (state) {
                    UiState.Loading -> {
                        //todo
                    }
                    is UiState.Error -> {
                        Log.i("DATA", state.exception.toString())
                    }
                    is UiState.Data -> {
                        Log.i("DATA", state.data.toString())
                        charactersAdapter.submitUpdate(state.data)
                    }
                }
            }
        })

       // charactersAdapter.submitUpdate(viewModel.list)

        return binding.root
    }
}