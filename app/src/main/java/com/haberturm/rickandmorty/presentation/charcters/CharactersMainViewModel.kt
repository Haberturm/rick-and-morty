package com.haberturm.rickandmorty.presentation.charcters

import androidx.lifecycle.ViewModel
import com.haberturm.rickandmorty.entities.CharacterUi

class CharactersMainViewModel : ViewModel() {

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