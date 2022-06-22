package com.haberturm.rickandmorty.presentation.locations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentLocationsMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LocationsMainFragment : DaggerFragment() {
    private lateinit var locationsAdapter: LocationListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val viewModel: LocationsMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[LocationsMainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationsAdapter = LocationListAdapter(
            listener = object : LocationListAdapter.ActionClickListener{
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
        val binding = FragmentLocationsMainBinding.inflate(inflater)
        binding.locationList.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = locationsAdapter
            addItemDecoration(
                GridSpacingItemDecoration(2,resources.getDimensionPixelSize(R.dimen.default_margin) , true, 0)
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
                        locationsAdapter.submitUpdate(state.data)
                    }
                }
            }
        })

        return binding.root
    }


}