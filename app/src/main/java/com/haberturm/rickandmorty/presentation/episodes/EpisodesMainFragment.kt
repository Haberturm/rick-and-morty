package com.haberturm.rickandmorty.presentation.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentEpisodesMainBinding
import com.haberturm.rickandmorty.databinding.FragmentLocationsMainBinding
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration

class EpisodesMainFragment : Fragment() {
    private lateinit var episodesAdapter: EpisodesListAdapter

    private val viewModel: EpisodesMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(EpisodesMainViewModel::class.java)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEpisodesMainBinding.inflate(inflater)
        binding.episodesList.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = episodesAdapter
            addItemDecoration(
                GridSpacingItemDecoration(2,resources.getDimensionPixelSize(R.dimen.default_margin) , true, 0)
            )
        }
        episodesAdapter.submitUpdate(viewModel.list)

        return binding.root
    }
}