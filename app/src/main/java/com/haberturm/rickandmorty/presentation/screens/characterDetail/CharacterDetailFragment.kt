package com.haberturm.rickandmorty.presentation.screens.characterDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharacterDetailBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.presentation.common.AlertDialogFragment
import com.haberturm.rickandmorty.presentation.common.ListFragmentMethods
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.presentation.decorators.MarginDecorator
import com.haberturm.rickandmorty.presentation.screens.charcters.CharactersMainFragment
import com.haberturm.rickandmorty.presentation.screens.episodes.EpisodesListAdapter
import com.haberturm.rickandmorty.util.Const
import com.haberturm.rickandmorty.util.Util
import com.haberturm.rickandmorty.util.loadImage
import com.haberturm.rickandmorty.util.setLinkText
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CharacterDetailFragment : DaggerFragment() {
    private lateinit var episodesAdapter: EpisodesListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val listFragmentMethods = ListFragmentMethods()

    private val viewModel: CharacterDetailViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CharacterDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodesAdapter = EpisodesListAdapter(
            listener = object : EpisodesListAdapter.ActionClickListener {
                override fun showDetail(id: Int) {

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
        val binding = FragmentCharacterDetailBinding.inflate(inflater)
        viewModel.getData(arguments?.getInt(Const.DETAIL_ID_ARG_KEY)!!)

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Character Details"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(null)
        }

        listFragmentMethods.recyclerViewTooling(
            recyclerView = binding.episodesList,
            manager = LinearLayoutManager(requireContext()),
            decorator = MarginDecorator(resources.getDimensionPixelSize(R.dimen.default_margin)),
            recyclerViewAdapter = episodesAdapter
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

                    binding.characterName.text = state.data.name
                    binding.characterImage.loadImage(state.data.image)
                    binding.characterAdditional.text = getString(
                        R.string.character_additional_template,
                        state.data.species,
                        state.data.gender,
                        state.data.status
                    )

                    val originText = getString(R.string.origin_template, state.data.originName)
                    binding.origin.setLinkText(
                        string = originText,
                        actionOnClick = {/*todo*/ },
                        linkStartPosition = Util.getNewWordPosition(originText),
                        linkEndPosition = originText.length
                    )

                    val locationText =
                        getString(R.string.location_template, state.data.locationName)
                    binding.location.setLinkText(
                        string = locationText,
                        actionOnClick = {/*todo*/ },
                        linkStartPosition = Util.getNewWordPosition(locationText),
                        linkEndPosition = locationText.length
                    )
                }
            }
        }

        viewModel.episodesListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> {
                    binding.episodesList.visibility = View.GONE
                    binding.loadingIndicatorList.visibility = View.VISIBLE
                    binding.errorList.root.visibility = View.GONE
                }
                is UiState.Error -> {
                    binding.episodesList.visibility = View.GONE
                    binding.loadingIndicatorList.visibility = View.GONE
                }
                is UiState.Data -> {
                    episodesAdapter.submitUpdate(state.data)
                    binding.errorList.root.visibility = View.GONE
                    binding.loadingIndicatorList.visibility = View.GONE
                    binding.episodesList.visibility = View.VISIBLE

                }
            }
        }
        return binding.root
    }
}


