package com.haberturm.rickandmorty.presentation.screens.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentEpisodesMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.presentation.common.AlertDialogFragment
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersFilterFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EpisodesMainFragment : DaggerFragment() {
    private lateinit var episodesAdapter: EpisodesListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val listFragmentMethods = ListFragmentMethods()

    private val viewModel: EpisodesMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[EpisodesMainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodesAdapter = EpisodesListAdapter(
            listener = object : EpisodesListAdapter.ActionClickListener{
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
        val binding = FragmentEpisodesMainBinding.inflate(inflater)

        listFragmentMethods.recyclerViewTooling(
            recyclerView = binding.episodesList,
            manager = GridLayoutManager(requireContext(),2),
            decorator = GridSpacingItemDecoration(2,resources.getDimensionPixelSize(R.dimen.default_margin) , true, 0),
            recyclerViewAdapter = episodesAdapter
        )

        listFragmentMethods.openFiltersButtonClickListener(
            button = binding.filtersButton,
            navManager = parentFragmentManager,
            filterFragment = EpisodesFilterFragment(),
            fragmentLabel = "EPISODES_FILTER"
        )

        listFragmentMethods.swipeToRefreshListener(
            swipeRefreshLayout = binding.swipeRefreshLayout,
            onRefreshAction = {viewModel.getData()}  //в нашем случае, не обязательно перезагружать фрагмент, можно просто обновить данные
        )

        listFragmentMethods.stateObserver(
            lifecycleOwner = viewLifecycleOwner,
            state = viewModel.uiState,
            recyclerView = binding.episodesList,
            recyclerViewAdapter = episodesAdapter,
            loadingIndicator = binding.loadingIndicator,
            errorView = binding.error,
            errorRefreshAction = {viewModel.getData()},
            fragmentManager = parentFragmentManager,
        )

        return binding.root
    }
}