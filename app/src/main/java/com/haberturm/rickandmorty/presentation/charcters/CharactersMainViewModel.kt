package com.haberturm.rickandmorty.presentation.charcters

import androidx.lifecycle.ViewModel
import com.haberturm.rickandmorty.entities.CharacterUi

class CharactersMainViewModel : ViewModel() {

    fun showDetails(){

    }


    val list = listOf<CharacterUi>(
        CharacterUi(
            1,
            "dddd",
            "ddddd",
            "flkfsg",
            "kg;kfd;kg",
           " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            2,
            "dddd",
            "ddddd",
            "flkfsg",
            "kg;kfd;kg",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            3,
            "dddd",
            "ddddd",
            "flkfsg",
            "kg;kfd;kg",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            4,
            "dddd",
            "ddddd",
            "flkfsg",
            "kg;kfd;kg",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        ),
        CharacterUi(
            5,
            "dddd",
            "ddddd",
            "flkfsg",
            "kg;kfd;kg",
            " https://rickandmortyapi.com/api/character/avatar/2.jpeg"
        )
    )
}