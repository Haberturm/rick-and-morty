package com.haberturm.rickandmorty.domain.repositories

import com.haberturm.rickandmorty.domain.entities.Characters

interface Repository {
    suspend fun getCharacters(): List<Characters>
}