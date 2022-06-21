package com.haberturm.rickandmorty.data.repositories

import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.data.mappers.characters.CharactersDataMapper
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {
    override suspend fun getCharacters(): ApiState<Characters> =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.retrofit.getDataList()
                if (response.isSuccessful){
                    return@withContext ApiState.Success(CharactersDataMapper().fromDataToDomain(response.body()))
                }else{
                    return@withContext ApiState.Error(Exception(response.message()))
                }
            }catch (e: Exception){
                return@withContext ApiState.Error(e)
            }

        }
}