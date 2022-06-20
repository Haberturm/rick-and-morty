package com.haberturm.rickandmorty.presentation.charcters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.data.api.RetrofitClient
import com.haberturm.rickandmorty.entities.CharacterUi
import kotlinx.coroutines.launch

class CharactersMainViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            Log.i("DATA", "${RetrofitClient.retrofit.getDataList()}")
        }

    }

    fun showDetails(){

    }


    val list = listOf<CharacterUi>(
        CharacterUi(
            0,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
           " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            1,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            2,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            3,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            4,
            "Morty Smith",
            "Alive",
            "Male",
            "Human",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
    )
}