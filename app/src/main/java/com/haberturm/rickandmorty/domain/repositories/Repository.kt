package com.haberturm.rickandmorty.domain.repositories

import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCharacters(): Flow<ApiState<Characters>>
    suspend fun updateCharacters(): Flow<ApiState<Unit>>

    suspend fun updateLocations(): Flow<ApiState<Unit>>
    suspend fun getLocations(): Flow<ApiState<Locations>>

    suspend fun updateEpisodes(): Flow<ApiState<Unit>>
    suspend fun getEpisodes(): Flow<ApiState<Episodes>>

}