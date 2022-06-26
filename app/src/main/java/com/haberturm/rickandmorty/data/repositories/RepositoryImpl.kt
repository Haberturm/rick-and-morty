package com.haberturm.rickandmorty.data.repositories

import com.haberturm.rickandmorty.data.api.RetrofitClient
import com.haberturm.rickandmorty.data.db.RickAndMortyDatabase
import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.characters.CharactersInfoData
import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesInfoData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResponseData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationsInfoData
import com.haberturm.rickandmorty.data.entities.locations.LocationsResponseData
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
import java.net.UnknownHostException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: RickAndMortyDatabase
) : Repository {

    override suspend fun updateCharacters(): Flow<ApiState<Unit>> = flow {
        emit(
            updateState(
                remoteDataSource = { RetrofitClient.retrofit.getCharacters() },
                insertDataInDB = fun(data: ArrayList<CharacterResultsData>) {
                    database.characterDao().insertAll(data)
                },
                insertInfoDataInDB = fun(info: CharactersInfoData) {
                    database.characterInfoDao().insertInfo(info)
                }
            )
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun getCharacters(): Flow<ApiState<Characters>> = flow {
        emit(
            dataState<Characters, List<CharacterResultsData>, CharactersInfoData>(
                mapper = CharactersDataMapper(),
                localDataSource = { database.characterDao().getAllCharacters() },
                localDataInfoSource = { database.characterInfoDao().getCharactersInfo() }
            )
        )
    }.flowOn(Dispatchers.IO)

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

    private suspend fun <D, T1, T2> updateState(
        remoteDataSource: suspend () -> Response<D>,
        insertDataInDB: (T1) -> Unit,
        insertInfoDataInDB: (T2) -> Unit,
    ): ApiState<Unit> {
        try {
            val response = remoteDataSource()
            val data = response.body()
            if (response.isSuccessful) {
                when (data) { //need for smart cast (да-да, получилось немного костыльно:( )
                    is CharactersResponseData -> {
                        insertDataInDB(data.results as T1)
                        insertInfoDataInDB(data.info as T2)
                    }
                    is LocationsResponseData -> {
                        insertDataInDB(data.results as T1)
                        insertInfoDataInDB(data.info as T2)
                    }
                    is EpisodesResponseData -> {
                        insertDataInDB(data.results as T1)
                        insertInfoDataInDB(data.info as T2)
                    }
                }
                return ApiState.Success(Unit)
            } else {
                return ApiState.Error(
                    AppException.NetworkException(response.raw().toString())
                )
            }
        } catch (e: Exception) {
            return if (e is UnknownHostException) { //в нашем случае(хост же у нас не поменяется, надеюсь:) )значит, что нет соединения с интернетом
                ApiState.Error(
                    AppException.NoInternetConnectionException(e.message.toString())
                )

            } else {
                ApiState.Error(
                    AppException.UnknownException(e.message.toString())
                )
            }
        }
    }

    private inline fun <reified R, R1, R2> dataState(
        mapper: DataMapper,
        localDataSource: () -> R1,
        localDataInfoSource: () -> R2,
        ): ApiState<R> {
        try {
            when (R::class) {
                Characters::class -> {
                    val data2return = CharactersResponseData(
                        info = localDataInfoSource() as CharactersInfoData,
                        results = localDataSource() as ArrayList<CharacterResultsData>
                    )
                    return ApiState.Success(
                        data = mapper.fromDataToDomain(data2return)
                    )
                }
                Locations::class -> {
                    val data2return = LocationsResponseData(
                        info = localDataInfoSource() as LocationsInfoData,
                        results = localDataSource() as ArrayList<LocationResultsData>
                    )
                    return ApiState.Success(
                        data = mapper.fromDataToDomain(data2return)
                    )
                }
                Episodes::class -> {
                    val data2return = EpisodesResponseData(
                        info = localDataInfoSource() as EpisodesInfoData,
                        results = localDataSource() as ArrayList<EpisodesResultsData>
                    )
                    return ApiState.Success(
                        data = mapper.fromDataToDomain(data2return)
                    )
                }
                else -> {
                    return ApiState.Error(
                        AppException.UnknownException("WRONG TYPE IN dataState")
                    )
                }
            }
        } catch (e: Exception) {
            return ApiState.Error(
                AppException.UnknownException(e.message.toString())
            )
        }
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



