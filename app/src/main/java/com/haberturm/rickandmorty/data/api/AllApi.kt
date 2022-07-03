package com.haberturm.rickandmorty.data.api

object AllApi {
    const val RICK_AND_MORTY_BASE_URL = "https://rickandmortyapi.com/api/"

    private const val CHARACTERS_PATH = "character/"
    const val CHARACTERS = RICK_AND_MORTY_BASE_URL + CHARACTERS_PATH
    private const val SINGLE_CHARACTERS_PATH = "character/{id}"
    const val SINGLE_CHARACTER = RICK_AND_MORTY_BASE_URL + SINGLE_CHARACTERS_PATH

    private const val LOCATIONS_PATH = "location/"
    const val LOCATIONS = RICK_AND_MORTY_BASE_URL + LOCATIONS_PATH

    private const val EPISODES_PATH = "episode/"
    const val EPISODES = RICK_AND_MORTY_BASE_URL + EPISODES_PATH

    private const val EPISODES_BY_IDS_PATH = "episode/{ids}"
    const val EPISODES_BY_IDS = RICK_AND_MORTY_BASE_URL + EPISODES_BY_IDS_PATH
}