package com.haberturm.rickandmorty.presentation.entities

data class LocationDetailUi(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val charactersIds: List<Int>
)
