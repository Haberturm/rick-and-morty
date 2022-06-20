package com.haberturm.rickandmorty.presentation.charcters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haberturm.data.api.RetrofitClient
import com.haberturm.data.repositories.RepositoryImpl
import com.haberturm.domain.repositories.Repository
import com.haberturm.rickandmorty.entities.CharacterUi
import kotlinx.coroutines.launch

class CharactersMainViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            //getRepo().getCharacters()
            Log.i("DATA", "${getRepo().getCharacters()}")
        }

    }

    fun showDetails(){

    }

    fun getRepo(): Repository {
        return RepositoryImpl()
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