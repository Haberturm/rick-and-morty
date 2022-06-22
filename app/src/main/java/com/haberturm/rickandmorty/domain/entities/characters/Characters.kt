package com.haberturm.rickandmorty.domain.entities.characters

data class Characters(
    var info: CharactersInfo,
    var results: ArrayList<CharacterResults>
)
