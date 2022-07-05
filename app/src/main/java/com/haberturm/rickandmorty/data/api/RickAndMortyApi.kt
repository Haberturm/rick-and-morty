package com.haberturm.rickandmorty.data.api

import com.haberturm.rickandmorty.data.entities.characters.CharacterResultsData
import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResponseData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationResultsData
import com.haberturm.rickandmorty.data.entities.locations.LocationsResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET(AllApi.CHARACTERS)
    suspend fun getCharacters(@Query("page") page:Int): Response<CharactersResponseData>
    @GET(AllApi.SINGLE_CHARACTER)
    suspend fun getSingleCharacter(@Path("id") id:Int): Response<CharacterResultsData>
    @GET(AllApi.CHARACTERS_BY_IDS)
    suspend fun getCharactersByIds(@Path("ids") ids: String): Response<ArrayList<CharacterResultsData>>

    @GET(AllApi.LOCATIONS)
    suspend fun getLocations(@Query("page") page:Int): Response<LocationsResponseData>
    @GET(AllApi.SINGLE_LOCATION)
    suspend fun getSingleLocation(@Path("id") id:Int): Response<LocationResultsData>

    @GET(AllApi.EPISODES)
    suspend fun getEpisodes(@Query("page") page:Int): Response<EpisodesResponseData>
    @GET(AllApi.SINGLE_EPISODE)
    suspend fun getSingleEpisode(@Path("id") id:Int): Response<EpisodesResultsData>
    @GET(AllApi.EPISODES_BY_IDS)
    suspend fun getEpisodesByIds(@Path("ids") ids: String): Response<ArrayList<EpisodesResultsData>>
}