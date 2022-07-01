package com.haberturm.rickandmorty.presentation.screens.characterDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.FragmentCharacterDetailBinding
import com.haberturm.rickandmorty.di.viewModel.ViewModelFactory
import com.haberturm.rickandmorty.presentation.common.UiState
import com.haberturm.rickandmorty.util.Util
import com.haberturm.rickandmorty.util.loadImage
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CharacterDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CharacterDetailViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CharacterDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharacterDetailBinding.inflate(inflater)

        Log.i("ARG", "${arguments?.getInt("string_key")}")
        viewModel.getData(arguments?.getInt("string_key")!!)

        viewModel.uiState.observe(viewLifecycleOwner){state->
            when(state){
                UiState.Loading -> {
                    //todo
                }
                is UiState.Error ->{
                    //todo
                }
                is UiState.Data -> {
                    binding.characterName.text = state.data.name
                    binding.characterImage.loadImage(state.data.image)
                    binding.characterAdditional.text = getString(
                        R.string.character_additional_template,
                        state.data.species,
                        state.data.gender,
                        state.data.status
                    )
                    Log.i("SINGLE_CHAR", "${state.data.episodesIds}")
                }

            }
        }
        return binding.root
    }
}