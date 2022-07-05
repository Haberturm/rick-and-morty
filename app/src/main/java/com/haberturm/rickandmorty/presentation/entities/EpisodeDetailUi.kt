package com.haberturm.rickandmorty.presentation.entities

data class EpisodeDetailUi(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val charactersIds: List<Int>
)
