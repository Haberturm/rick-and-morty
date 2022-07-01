package com.haberturm.rickandmorty.presentation.entities

data class CharacterDetailUi(
    val id: Int,
    val name: String,
    val status: String,
    val gender: String,
    val species: String,
    val image: String,
    val locationName: String,
    val locationId: Int,
    val originName: String,
    val originId: Int,
    val episodesIds: List<Int>
)
