package com.haberturm.rickandmorty.presentation.screens.locationDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentLocationDetailBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.presentation.common.AlertDialogFragment
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import com.haberturm.rickandmorty.presentation.decorators.MarginDecorator
import com.haberturm.rickandmorty.presentation.screens.characterDetail.CharacterDetailViewModel
import com.haberturm.rickandmorty.presentation.screens.charcters.CharacterListAdapter
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesListAdapter
import com.haberturm.rickandmorty.util.Const
import com.haberturm.rickandmorty.util.Util
import com.haberturm.rickandmorty.util.loadImage
import com.haberturm.rickandmorty.util.setLinkText
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LocationDetailFragment : DaggerFragment() {
    private lateinit var charactersAdapter: CharacterListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LocationDetailViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[LocationDetailViewModel ::class.java]
    }

    private val listFragmentMethods = ListFragmentMethods()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charactersAdapter  = CharacterListAdapter(
            listener = object : CharacterListAdapter.ActionClickListener {
                override fun showDetail(id: Int) {
                    //todo
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
        val binding = FragmentLocationDetailBinding.inflate(inflater)
        viewModel.getData(arguments?.getInt(Const.DETAIL_ID_ARG_KEY)!!)

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.location_details_tittle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(null)
        }

        listFragmentMethods.recyclerViewTooling(
            recyclerView = binding.charactersList,
            manager = GridLayoutManager(requireContext(), 2),
            decorator = GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.small_margin),
                true,
                0
            ),
            recyclerViewAdapter = charactersAdapter
        )


        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> {
                    binding.detailContent.visibility = View.GONE
                    binding.loadingIndicator.visibility = View.VISIBLE
                    binding.error.root.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.detailContent.visibility = View.GONE
                    binding.loadingIndicator.visibility = View.GONE
                    if (state.exception is AppException.NoInternetConnectionException) {
                        val alertDialogFragment = AlertDialogFragment()
                        alertDialogFragment.show(parentFragmentManager, "NO_INTERNET")
                    } else {
                        binding.error.root.visibility = View.VISIBLE
                        binding.error.errorRefreshButton.setOnClickListener {
                            viewModel.getData(arguments?.getInt(Const.DETAIL_ID_ARG_KEY)!!)
                        }
                    }
                }
                is UiState.Data -> {
                    binding.detailContent.visibility = View.VISIBLE
                    binding.loadingIndicator.visibility = View.GONE
                    binding.error.root.visibility = View.GONE

                    binding.locationName.text = state.data.name
                    binding.locationAdditional.text =
                        getString(
                            R.string.location_additional_template,
                            state.data.type,
                            state.data.dimension
                        )
                }
            }
        }

        listFragmentMethods.stateObserver(
            lifecycleOwner = viewLifecycleOwner,
            state = viewModel.charactersListState,
            recyclerView = binding.charactersList,
            loadingIndicator = binding.loadingIndicatorList,
            errorView = binding.errorList,
            errorRefreshAction = {viewModel.getData(arguments?.getInt(Const.DETAIL_ID_ARG_KEY)!!)},
            fragmentManager = parentFragmentManager,
            recyclerViewAdapter = charactersAdapter
        )

        return binding.root
    }
}