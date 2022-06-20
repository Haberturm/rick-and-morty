package com.haberturm.domain.repositories

import com.haberturm.domain.entities.Characters

interface Repository {
    suspend fun getCharacters(): List<Characters>
}