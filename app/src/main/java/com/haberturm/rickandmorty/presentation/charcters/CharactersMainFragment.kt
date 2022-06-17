package com.haberturm.rickandmorty.presentation.charcters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharactersMainBinding
import com.haberturm.rickandmorty.presentation.decorators.GridSpacingItemDecoration

class CharactersMainFragment : Fragment() {
    private lateinit var charactersAdapter: CharacterListAdapter

    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CharactersMainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charactersAdapter = CharacterListAdapter(
            listener = object : CharacterListAdapter.ActionClickListener{
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
        val binding = FragmentCharactersMainBinding.inflate(inflater)
        binding.charactersList.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = charactersAdapter
            addItemDecoration(
                GridSpacingItemDecoration(2,resources.getDimensionPixelSize(R.dimen.small_margin) , true, 0)
            )
        }
        charactersAdapter.submitUpdate(viewModel.list)

        return binding.root
    }
}