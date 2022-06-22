package com.haberturm.rickandmorty.data.api

object AllApi {
    const val RICK_AND_MORTY_BASE_URL = "https://rickandmortyapi.com/api/"

    private const val CHARACTERS_PATH = "character/"
    const val CHARACTERS = RICK_AND_MORTY_BASE_URL + CHARACTERS_PATH

    private const val LOCATIONS_PATH = "location/"
    const val LOCATIONS = RICK_AND_MORTY_BASE_URL + LOCATIONS_PATH
}