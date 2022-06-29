package com.haberturm.rickandmorty.data.api

import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.data.entities.episodes.EpisodesResponseData
import com.haberturm.rickandmorty.data.entities.locations.LocationsResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET(AllApi.CHARACTERS)
    suspend fun getCharacters(@Query("page") page:Int): Response<CharactersResponseData>

    @GET(AllApi.LOCATIONS)
    suspend fun getLocations(): Response<LocationsResponseData>

    @GET(AllApi.EPISODES)
    suspend fun getEpisodes(): Response<EpisodesResponseData>
}