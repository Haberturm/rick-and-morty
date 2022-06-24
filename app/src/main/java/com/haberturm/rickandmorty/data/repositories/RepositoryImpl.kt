package com.haberturm.rickandmorty.data.repositories

import android.content.Context
import android.util.Log
import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.data.db.RickAndMortyDatabase
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
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: RickAndMortyDatabase
) : Repository {


    override suspend fun getCharacters(): ApiState<Characters> =
        withContext(Dispatchers.IO) {
            val dao = database.characterDao()
            val data = RetrofitClient.retrofit.getCharacters()
            dao.insertAll(data.body()!!.results)
            val characters =  dao.getCharactersInRange(1,10)
            characters.forEach {
                Log.i("DBDATA", it.id.toString())
            }




            return@withContext stateWrapper(
                getData= {RetrofitClient.retrofit.getCharacters()},
                mapper = CharactersDataMapper()
            )
        }

    override suspend fun getLocations(): ApiState<Locations> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                getData = {RetrofitClient.retrofit.getLocations()},
                mapper = LocationsDataMapper()
            )
        }

    override suspend fun getEpisodes(): ApiState<Episodes> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                getData = {RetrofitClient.retrofit.getEpisodes()},
                mapper = EpisodesDataMapper()
            )
        }


    private suspend fun <T, D>stateWrapper(getData: suspend () -> Response<D>, mapper: DataMapper) : ApiState<T>{
        return try {
            val response = getData()
            if (response.isSuccessful){
                ApiState.Success(mapper.fromDataToDomain(response.body()))
            }else{
                ApiState.Error(Exception(response.raw().toString()))
            }
        }catch (e: Exception){
            ApiState.Error(e)
        }
    }
}



