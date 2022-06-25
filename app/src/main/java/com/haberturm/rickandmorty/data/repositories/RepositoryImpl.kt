package com.haberturm.rickandmorty.data.repositories

import android.util.Log
import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.data.db.RickAndMortyDatabase
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.data.mappers.DataMapper
import com.haberturm.rickandmorty.data.mappers.characters.CharactersDataMapper
import com.haberturm.rickandmorty.data.mappers.episodes.EpisodesDataMapper
import com.haberturm.rickandmorty.data.mappers.locations.LocationsDataMapper
import com.haberturm.rickandmorty.domain.common.ApiState
import com.haberturm.rickandmorty.domain.common.AppException
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: RickAndMortyDatabase
) : Repository {

    override suspend fun updateCharacters(): Flow<ApiState<Unit>> = flow {
        val characterDao = database.characterDao()
        val characterInfoDao = database.characterInfoDao()
        try {
            val response = RetrofitClient.retrofit.getCharacters()
            if (response.isSuccessful) {

                characterDao.insertAll(response.body()!!.results)
                characterInfoDao.insertInfo(response.body()!!.info)
                Log.i("DATA2", "${response.body()!!.results}")
                emit(
                    ApiState.Success(Unit)
                )
            } else {
                Log.i("DATA2", "${response.body()!!.results}")
                emit(
                    ApiState.Error(
                        AppException.NetworkException(response.raw().toString())
                    )
                )
            }
        } catch (e: Exception) {
            Log.i("DATA2", "exception: $e")
            emit(
                ApiState.Error(
                    AppException.UnknownException(e.message.toString())
                )
            )
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun getCharacters(): Flow<ApiState<Characters>> = flow {
        val characterDao = database.characterDao()
        val characterInfoDao = database.characterInfoDao()
        try {
            Log.i("DATA2", "${characterDao.getCharactersInRange(
                1,
                20
            )}")
            val data2return = CharactersResponseData(
                info = characterInfoDao.getCharactersInfo(),
                results = characterDao.getCharactersInRange(
                    1,
                    20
                ) as ArrayList<CharacterResultsData>
            )
            emit(
                ApiState.Success<Characters>(
                    data = CharactersDataMapper().fromDataToDomain(data2return)
                )
            )
        } catch (e: Exception) {
            emit(
                ApiState.Error(
                    AppException.UnknownException(e.message.toString())
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    /*
        override suspend fun getCharacters(): Flow<ApiState<Characters>> = flow{
        val characterDao = database.characterDao()
        val characterInfoDao = database.characterInfoDao()
        try {
            val response = RetrofitClient.retrofit.getCharacters()
            if (response.isSuccessful){
                characterDao.insertAll(response.body()!!.results)
                characterInfoDao.insertInfo(response.body()!!.info)
                val data2return = CharactersResponseData(
                    info = characterInfoDao.getCharactersInfo(),
                    results = characterDao.getCharactersInRange(1,20) as ArrayList<CharacterResultsData>
                )
                emit(
                    ApiState.Success<Characters>(data = CharactersDataMapper().fromDataToDomain(data2return))
                )
            }else{
                emit(
                    ApiState.Error(
                        AppException.NetworkException(response.raw().toString())
                    )
                )
            }
        }catch (e: Exception){
            emit(ApiState.Error(
                AppException.UnknownException(e.message.toString())
            ))
        }
    }.flowOn(Dispatchers.IO)

     */

    override suspend fun getLocations(): ApiState<Locations> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                getData = { RetrofitClient.retrofit.getLocations() },
                mapper = LocationsDataMapper()
            )
        }

    override suspend fun getEpisodes(): ApiState<Episodes> =
        withContext(Dispatchers.IO) {
            return@withContext stateWrapper(
                getData = { RetrofitClient.retrofit.getEpisodes() },
                mapper = EpisodesDataMapper()
            )
        }

    private suspend fun <T, D> stateWrapper(
        getData: suspend () -> Response<D>,
        mapper: DataMapper
    ): ApiState<T> {
        return try {
            val response = getData()
            if (response.isSuccessful) {
                ApiState.Success(mapper.fromDataToDomain(response.body()))
            } else {
                ApiState.Error(
                    AppException.NetworkException(response.raw().toString())
                )
            }
        } catch (e: Exception) {
            ApiState.Error(
                AppException.UnknownException(e.message.toString())
            )
        }
    }
}



