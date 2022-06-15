package com.haberturm.rickandmorty.presentation.charcters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.haberturm.rickandmorty.databinding.FragmentCharactersMainBinding

class CharactersMainFragment : Fragment() {

    private val viewModel: CharactersMainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CharactersMainViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharactersMainBinding.inflate(inflater)

        return binding.root
    }
}