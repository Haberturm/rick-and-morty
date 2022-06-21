package com.haberturm.rickandmorty.data.repositories

import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.domain.entities.Characters
import com.haberturm.rickandmorty.domain.repositories.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {
    override suspend fun getCharacters(): List<Characters> {
        return RetrofitClient.retrofit.getDataList().results.map {
            Characters(
                id = it.id!!
            )
        }
    }
}