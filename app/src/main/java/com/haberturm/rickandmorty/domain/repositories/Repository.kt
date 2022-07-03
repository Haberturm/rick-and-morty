package com.haberturm.rickandmorty.domain.repositories

import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.CharacterResults
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCharacters(page: Int): Flow<ApiState<Characters>>
    fun updateCharacters(page: Int): Flow<ApiState<Unit>>
    fun getFilteredCharacters(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<ApiState<Characters>>
    fun updateSingleCharacter(id: Int): Flow<ApiState<Unit>>
    fun getSingleCharacter(id: Int): Flow<ApiState<CharacterResults>>

    fun updateLocations(page: Int): Flow<ApiState<Unit>>
    fun getLocations(page: Int): Flow<ApiState<Locations>>
    fun getFilteredLocations(
        name: String,
        dimension: String,
        type: String
    ): Flow<ApiState<Locations>>

    fun updateEpisodes(page: Int): Flow<ApiState<Unit>>
    fun getEpisodes(page: Int): Flow<ApiState<Episodes>>
    fun getFilteredEpisodes(
        name: String,
        episodes: String
    ): Flow<ApiState<Episodes>>
    fun getEpisodesByIdList(ids: List<Int>): Flow<ApiState<Episodes>>
    fun updateEpisodesByIdList(ids: List<Int>): Flow<ApiState<Unit>>

}