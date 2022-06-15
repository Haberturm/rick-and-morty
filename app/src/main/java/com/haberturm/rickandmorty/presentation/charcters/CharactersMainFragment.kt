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
import com.haberturm.rickandmorty.presentation.decorators.MarginDecorator

class CharactersMainFragment : Fragment() {
    private lateinit var charactersAdapter: CharacterListAdapter

    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CharactersMainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charactersAdapter = CharacterListAdapter(
            object : CharacterListAdapter.ActionClickListener{
                override fun showDetail(id: Int) {
                    viewModel.showDetails()
                }
            }
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
                    MarginDecorator(resources.getDimensionPixelSize(R.dimen.small_margin)))
        }
        charactersAdapter.submitUpdate(viewModel.list)

        return binding.root
    }
}