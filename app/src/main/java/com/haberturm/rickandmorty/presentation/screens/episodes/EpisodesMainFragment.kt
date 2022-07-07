package com.haberturm.rickandmorty.presentation.screens.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentEpisodesMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import com.haberturm.rickandmorty.presentation.navigation.Navigation
import com.haberturm.rickandmorty.presentation.screens.episodeDetail.EpisodeDetailFragment
import com.haberturm.rickandmorty.util.Const
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EpisodesMainFragment : DaggerFragment() {
    private lateinit var episodesAdapter: EpisodesListAdapter
    private lateinit var navigation: Navigation

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val listFragmentMethods = ListFragmentMethods()

    private val viewModel: EpisodesMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[EpisodesMainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation = Navigation(parentFragmentManager)
        episodesAdapter = EpisodesListAdapter(
            listener = object : EpisodesListAdapter.ActionClickListener {
                override fun showDetail(id: Int) {
                    navigation.replaceFragment(
                        containerId = R.id.fullscreen_container,
                        fragment = EpisodeDetailFragment(),
                        arguments = Bundle().apply { putInt(Const.DETAIL_ID_ARG_KEY, id) },
                        addToBackStack = Const.EPISODE_DETAIL_FRAGMENT
                    )
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
        val binding = FragmentEpisodesMainBinding.inflate(inflater)

        viewModel.currentPage.observe(viewLifecycleOwner) { currentPage ->
            viewModel.maxPages.observe(viewLifecycleOwner) { maxPages ->
                binding.pageHeaderText.text =
                    getString(R.string.page_header_text, currentPage, maxPages)
            }
        }

        listFragmentMethods.setUpPagePicker(
            pagePicker = binding.pagePicker,
            onNextPage = { viewModel.nextPage() },
            onPreviousPage = { viewModel.previousPage() },
            jumpToPage = fun(pageText: CharSequence) {
                viewModel.jumpToPage(pageText)
            },
            jumpToPageState = viewModel.jumpToPageEditState,
            previousPageState = viewModel.previousPageState,
            nextPageState = viewModel.nextPageState,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext()
        )

        listFragmentMethods.recyclerViewTooling(
            recyclerView = binding.episodesList,
            manager = GridLayoutManager(requireContext(), 2),
            decorator = GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.default_margin),
                true,
                0
            ),
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
            onRefreshAction = { viewModel.refreshData() }
        )

        listFragmentMethods.stateObserver(
            lifecycleOwner = viewLifecycleOwner,
            state = viewModel.uiState,
            recyclerView = binding.episodesList,
            recyclerViewAdapter = episodesAdapter,
            loadingIndicator = binding.loadingIndicator,
            errorView = binding.error,
            errorRefreshAction = { viewModel.refreshData() },
            fragmentManager = parentFragmentManager,
            getString = fun(id: Int): String {
                return getString(id)
            }
        )

        return binding.root
    }
}