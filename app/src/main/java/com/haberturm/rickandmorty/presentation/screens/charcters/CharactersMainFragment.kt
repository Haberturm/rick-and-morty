package com.haberturm.rickandmorty.presentation.screens.charcters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharactersMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.presentation.common.AlertDialogFragment
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
                        binding.loadingIndicator.visibility = View.VISIBLE
                        binding.error.root.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        binding.loadingIndicator.visibility = View.GONE
                        if (state.exception is AppException.NoInternetConnectionException){
                            val alertDialogFragment = AlertDialogFragment()
                            val manager = parentFragmentManager
                            alertDialogFragment.show(manager,"NO_INTERNET")
                        }else{
                            binding.error.root.visibility = View.VISIBLE
                            binding.error.errorRefreshButton.setOnClickListener {
                                viewModel.getData()
                            }
                        }
                    }
                    is UiState.Data -> {
                        binding.loadingIndicator.visibility = View.GONE
                        binding.error.root.visibility = View.GONE
                        charactersAdapter.submitUpdate(state.data)
                    }
                }
            }
        })

        return binding.root
    }
}