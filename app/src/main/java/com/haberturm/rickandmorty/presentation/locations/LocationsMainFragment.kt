package com.haberturm.rickandmorty.presentation.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentLocationsMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.charcters.CharactersMainViewModel
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
        locationsAdapter.submitUpdate(viewModel.list)

        return binding.root
    }


}