package com.haberturm.rickandmorty.domain.repositories

import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters

interface Repository {
    suspend fun getCharacters(): ApiState<Characters>
}