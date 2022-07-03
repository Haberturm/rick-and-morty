package com.haberturm.rickandmorty.presentation.screens.charcters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharactersMainBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration
import com.haberturm.rickandmorty.presentation.screens.characterDetail.CharacterDetailFragment
import com.haberturm.rickandmorty.util.Const
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
                    parentFragmentManager.commit {
                        val arguments = Bundle()
                        arguments.putInt( Const.DETAIL_ID_ARG_KEY , id);
                        val fragment = CharacterDetailFragment()
                        fragment.arguments = arguments
                        replace(R.id.fullscreen_container, fragment)
                        addToBackStack("CHARACTER_DETAIL_FRAGMENT")
                        setReorderingAllowed(true)
                    }
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
        val binding = FragmentCharactersMainBinding.inflate(inflater)

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