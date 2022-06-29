package com.haberturm.rickandmorty.presentation.screens.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentLocationsMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.presentation.common.AlertDialogFragment
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersFilterFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LocationsMainFragment : DaggerFragment() {
    private lateinit var locationsAdapter: LocationListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val listFragmentMethods = ListFragmentMethods()

    private val viewModel: LocationsMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[LocationsMainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationsAdapter = LocationListAdapter(
            listener = object : LocationListAdapter.ActionClickListener {
                override fun showDetail(id: Int) {
                    viewModel.showDetails()
                }
            },
            context = requireContext()
        )
        //viewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLocationsMainBinding.inflate(inflater)

        viewModel.currentPage.observe(viewLifecycleOwner){ currentPage ->
            viewModel.maxPages.observe(viewLifecycleOwner){ maxPages ->
                binding.pageHeaderText.text = getString(R.string.page_header_text,currentPage, maxPages)
            }

        }

        listFragmentMethods.setUpPagePicker(
            pagePicker = binding.pagePicker,
            onNextPage = { viewModel.nextPage() },
            onPreviousPage = { viewModel.previousPage() },
            jumpToPage = fun(pageText: CharSequence){
                viewModel.jumpToPage(pageText)
            },
            jumpToPageState = viewModel.jumpToPageEditState,
            previousPageState = viewModel.previousPageState,
            nextPageState = viewModel.nextPageState,
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext()
        )

        listFragmentMethods.openFiltersButtonClickListener(
            button = binding.filtersButton,
            navManager = parentFragmentManager,
            filterFragment = LocationsFilterFragment(),
            fragmentLabel = "LOCATIONS_FILTER"
        )

        listFragmentMethods.recyclerViewTooling(
            recyclerView = binding.locationList,
            manager = GridLayoutManager(requireContext(), 2),
            decorator = GridSpacingItemDecoration(
                spanCount = 2,
                spacing = resources.getDimensionPixelSize(R.dimen.default_margin),
                includeEdge = true,
                headerNum = 0
            ),
            recyclerViewAdapter = locationsAdapter
        )

        listFragmentMethods.swipeToRefreshListener(
            swipeRefreshLayout = binding.swipeRefreshLayout,
            onRefreshAction = { viewModel.refreshData() }  //в нашем случае, не обязательно перезагружать фрагмент, можно просто обновить данные
        )

        listFragmentMethods.stateObserver(
            lifecycleOwner = viewLifecycleOwner,
            state = viewModel.uiState,
            recyclerView = binding.locationList,
            recyclerViewAdapter = locationsAdapter,
            loadingIndicator = binding.loadingIndicator,
            errorView = binding.error,
            errorRefreshAction = { viewModel.getData() },
            fragmentManager = parentFragmentManager,
        )

        return binding.root
    }
}