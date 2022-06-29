package com.haberturm.rickandmorty.presentation.screens.charcters

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.ErrorLayoutBinding
import com.haberturm.rickandmorty.databinding.FragmentCharactersMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.presentation.common.AlertDialogFragment
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.presentation.entities.LocationUi
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesListAdapter
import com.haberturm.rickandmorty.presentation.screens.locations.LocationListAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CharactersMainFragment : DaggerFragment() {
    private lateinit var charactersAdapter: CharacterListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val listFragmentMethods = ListFragmentMethods()

    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CharactersMainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charactersAdapter = CharacterListAdapter(
            listener = object : CharacterListAdapter.ActionClickListener {
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
        val binding = FragmentCharactersMainBinding.inflate(inflater)
        
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
            filterFragment = CharactersFilterFragment(),
            fragmentLabel = "CHARACTERS_FILTER"
        )

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

        listFragmentMethods.swipeToRefreshListener(
            swipeRefreshLayout = binding.swipeRefreshLayout,
            onRefreshAction = { viewModel.refreshData() }
        )

        listFragmentMethods.stateObserver(
            lifecycleOwner = viewLifecycleOwner,
            state = viewModel.uiState,
            recyclerView = binding.charactersList,
            recyclerViewAdapter = charactersAdapter,
            loadingIndicator = binding.loadingIndicator,
            errorView = binding.error,
            errorRefreshAction = { viewModel.getData() },
            fragmentManager = parentFragmentManager,
        )

        return binding.root
    }
}