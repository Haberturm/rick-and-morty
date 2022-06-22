package com.haberturm.rickandmorty.domain.repositories

import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations

interface Repository {
    suspend fun getCharacters(): ApiState<Characters>
    suspend fun getLocations(): ApiState<Locations>
    suspend fun getEpisodes(): ApiState<Episodes>
}