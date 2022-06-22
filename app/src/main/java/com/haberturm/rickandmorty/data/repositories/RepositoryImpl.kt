package com.haberturm.rickandmorty.data.repositories

import android.util.Log
import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.data.mappers.DataMapper
import com.haberturm.rickandmorty.data.mappers.characters.CharactersDataMapper
import com.haberturm.rickandmorty.data.mappers.episodes.EpisodesDataMapper
import com.haberturm.rickandmorty.data.mappers.locations.LocationsDataMapper
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {
    override suspend fun getCharacters(): ApiState<Characters> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                response = RetrofitClient.retrofit.getCharacters(),
                mapper = CharactersDataMapper()
            )
        }

    override suspend fun getLocations(): ApiState<Locations> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                response = RetrofitClient.retrofit.getLocations(),
                mapper = LocationsDataMapper()
            )
        }

    override suspend fun getEpisodes(): ApiState<Episodes> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                response = RetrofitClient.retrofit.getEpisodes(),
                mapper = EpisodesDataMapper()
            )
        }
}



fun <T, D>stateWrapper(response: Response<D>, mapper: DataMapper) : ApiState<T>{
    return try {
        if (response.isSuccessful){
            ApiState.Success(mapper.fromDataToDomain(response.body()))
        }else{
            ApiState.Error(Exception(response.raw().toString()))
        }
    }catch (e: Exception){
        ApiState.Error(e)
    }
}