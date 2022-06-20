package com.haberturm.data.repositories

import com.haberturm.data.api.RetrofitClient
import com.haberturm.domain.entities.Characters
import com.haberturm.domain.repositories.Repository

class RepositoryImpl : Repository {
    override suspend fun getCharacters(): List<Characters> {
        return RetrofitClient.retrofit.getDataList().results.map {
            Characters(
                id = it.id!!
            )
        }
    }
}