package com.haberturm.rickandmorty.data.repositories

import android.util.Log
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
import com.haberturm.rickandmorty.domain.entities.characters.CharacterResults
import com.haberturm.rickandmorty.domain.entities.characters.Characters
import com.haberturm.rickandmorty.domain.entities.episodes.Episodes
import com.haberturm.rickandmorty.domain.entities.locations.Locations
import com.haberturm.rickandmorty.domain.repositories.Repository
import com.haberturm.rickandmorty.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val database: RickAndMortyDatabase
) : Repository {

    override fun updateCharacters(page: Int): Flow<ApiState<Unit>> = flow {
        emit(
            updateState(
                remoteDataSource = { RetrofitClient.retrofit.getCharacters(page) },
                insertDataInDB = fun(data: ArrayList<CharacterResultsData>) {
                    database.characterDao().insertAll(data)
                },
                insertInfoDataInDB = fun(info: CharactersInfoData) {
                    database.characterInfoDao().insertInfo(info)
                }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getCharacters(page: Int): Flow<ApiState<Characters>> = flow {
        val upperBound = page * Const.ITEMS_PER_PAGE
        val lowerBound = upperBound - Const.ITEMS_PER_PAGE + 1
        emit(
            dataState<Characters, List<CharacterResultsData>, CharactersInfoData>(
                mapper = CharactersDataMapper(),
                localDataSource = { database.characterDao().getCharactersInRange(lowerBound, upperBound) },
                localDataInfoSource = { database.characterInfoDao().getCharactersInfo() }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getFilteredCharacters(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<ApiState<Characters>> = flow {
        emit(
            dataState<Characters, List<CharacterResultsData>, CharactersInfoData>(
                mapper = CharactersDataMapper(),
                localDataSource = {
                    database.characterDao().getFilteredCharacters(
                        name = name,
                        status = status.ifEmpty { "%" },
                        species = species,
                        type = type,
                        gender = gender.ifEmpty { "%" }
                    )
                },
                localDataInfoSource = { database.characterInfoDao().getCharactersInfo() }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun updateSingleCharacter(id: Int): Flow<ApiState<Unit>> = flow<ApiState<Unit>> {
        emit(
            updateState(
                remoteDataSource = {RetrofitClient.retrofit.getSingleCharacters(id)},
                insertDataInDB = fun(data: ArrayList<CharacterResultsData>) {
                    database.characterDao().insertAll(data)
                },
                insertInfoDataInDB = fun(_: Unit) {
                    Unit
                },
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getSingleCharacter(id: Int): Flow<ApiState<CharacterResults>> = flow<ApiState<CharacterResults>> {
        emit(
            dataState(
                mapper = CharactersDataMapper(),
                localDataSource = {database.characterDao().getCharacterById(id)},
                localDataInfoSource = {}
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun updateLocations(page: Int): Flow<ApiState<Unit>> = flow {
        emit(
            updateState(
                remoteDataSource = { RetrofitClient.retrofit.getLocations(page) },
                insertDataInDB = fun(data: ArrayList<LocationResultsData>) {
                    database.locationsDao().insertAll(data)
                },
                insertInfoDataInDB = fun(info: LocationsInfoData) {
                    database.locationsInfoDao().insertInfo(info)
                }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getLocations(page: Int): Flow<ApiState<Locations>> = flow {
        val upperBound = page * Const.ITEMS_PER_PAGE
        val lowerBound = upperBound - Const.ITEMS_PER_PAGE + 1
        emit(
            dataState<Locations, List<LocationResultsData>, LocationsInfoData>(
                mapper = LocationsDataMapper(),
                localDataSource = { database.locationsDao().getLocationsInRange(lowerBound, upperBound) },
                localDataInfoSource = { database.locationsInfoDao().getLocationsInfo() }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getFilteredLocations(
        name: String,
        dimension: String,
        type: String
    ): Flow<ApiState<Locations>> = flow {
        emit(dataState<Locations, List<LocationResultsData>, LocationsInfoData>(
            mapper = LocationsDataMapper(),
            localDataSource = { database.locationsDao().getFilteredLocations(
                name = name,
                dimension = dimension,
                type = type
            ) },
            localDataInfoSource = { database.locationsInfoDao().getLocationsInfo() }
        ))
    }.flowOn(Dispatchers.IO)

    override fun updateEpisodes(page: Int): Flow<ApiState<Unit>> = flow<ApiState<Unit>> {
        emit(
            updateState(
                remoteDataSource = { RetrofitClient.retrofit.getEpisodes(page) },
                insertDataInDB = fun(data: ArrayList<EpisodesResultsData>) {
                    database.episodesDao().insertAll(data)
                },
                insertInfoDataInDB = fun(info: EpisodesInfoData) {
                    database.episodesInfoDao().insertInfo(info)
                }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getEpisodes(page: Int): Flow<ApiState<Episodes>> = flow<ApiState<Episodes>> {
        val upperBound = page * Const.ITEMS_PER_PAGE
        val lowerBound = upperBound - Const.ITEMS_PER_PAGE + 1
        emit(
            dataState<Episodes, List<EpisodesResultsData>, EpisodesInfoData>(
                mapper = EpisodesDataMapper(),
                localDataSource = { database.episodesDao().getEpisodesInRange(lowerBound, upperBound) },
                localDataInfoSource = { database.episodesInfoDao().getEpisodesInfo() }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getFilteredEpisodes(name: String, episodes: String) = flow {
        emit(
            dataState<Episodes, List<EpisodesResultsData>, EpisodesInfoData>(
                mapper = EpisodesDataMapper(),
                localDataSource = { database.episodesDao().getFilteredEpisodes(
                    name = name,
                    episodes = episodes
                ) },
                localDataInfoSource = { database.episodesInfoDao().getEpisodesInfo() }
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun getEpisodesByIdList(ids: List<Int>) = flow{
        emit(
            dataState<Episodes, List<EpisodesResultsData>, EpisodesInfoData?>(
                mapper = EpisodesDataMapper(),
                localDataSource = { database.episodesDao().getEpisodesByIdList(ids) },
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun updateEpisodesByIdList(ids: List<Int>): Flow<ApiState<Unit>> = flow<ApiState<Unit>> {
        val idsPath = ids.toString()
        emit(
            updateState(
                remoteDataSource = { RetrofitClient.retrofit.getEpisodesByIds(idsPath) },
                insertDataInDB = fun(data: ArrayList<EpisodesResultsData>) {
                    database.episodesDao().insertAll(data)
                },
                insertInfoDataInDB = fun(_: Unit) {
                    Unit
                }
            )
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun <D, T1, T2> updateState(
        remoteDataSource: suspend () -> Response<D>,
        insertDataInDB: (T1) -> Unit,
        insertInfoDataInDB: ((T2) -> Unit)? = null,
    ): ApiState<Unit> {
        try {
            val response = remoteDataSource()
            val data = response.body()
            if (response.isSuccessful) {
                when (data) { //need for smart cast (да-да, получилось немного костыльно:( )
                    is CharactersResponseData -> {
                        insertDataInDB(data.results as T1)
                        if (insertInfoDataInDB != null) {
                            insertInfoDataInDB(data.info as T2)
                        }
                    }
                    is LocationsResponseData -> {
                        insertDataInDB(data.results as T1)
                        if (insertInfoDataInDB != null) {
                            insertInfoDataInDB(data.info as T2)
                        }
                    }
                    is EpisodesResponseData -> {
                        insertDataInDB(data.results as T1)
                        if (insertInfoDataInDB != null) {
                            insertInfoDataInDB(data.info as T2)
                        }
                    }
                    is CharacterResultsData -> {
                        val data2insert = arrayListOf<CharacterResultsData >(data)
                        insertDataInDB(data2insert as T1)
                    }
                    is List<*> -> {
                        try {
                            when(data[0]){
                                is EpisodesResultsData -> {
                                    insertDataInDB(data as T1)
                                }
                            }
                        }catch (e: Exception){
                            return ApiState.Error(
                                AppException.UnknownException(e.message.toString())
                            )
                        }
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
        noinline localDataInfoSource: (() -> R2)? = null,
    ): ApiState<R> {
        try {
            when (R::class) {
                Characters::class -> {
                    val data2return = CharactersResponseData(
                        info = if(localDataInfoSource != null) localDataInfoSource() as CharactersInfoData else null,
                        results = localDataSource() as ArrayList<CharacterResultsData>
                    )
                    return ApiState.Success(
                        data = mapper.fromDataToDomain(data2return)
                    )
                }
                Locations::class -> {
                    val data2return = LocationsResponseData(
                        info = if(localDataInfoSource!=null)localDataInfoSource() as LocationsInfoData else null,
                        results = localDataSource() as ArrayList<LocationResultsData>
                    )
                    return ApiState.Success(
                        data = mapper.fromDataToDomain(data2return)
                    )
                }
                Episodes::class -> {
                    val data2return = EpisodesResponseData(
                        info = if(localDataInfoSource != null)  localDataInfoSource() as EpisodesInfoData else null,
                        results = localDataSource() as ArrayList<EpisodesResultsData>
                    )
                    return ApiState.Success(
                        data = mapper.fromDataToDomain(data2return)
                    )
                }
                CharacterResults::class -> {
                    val data2return = localDataSource() as CharacterResultsData
                    return ApiState.Success(
                        data = mapper.fromDataToDomainSingle(data2return)
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
}



