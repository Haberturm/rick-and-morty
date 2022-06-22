package com.haberturm.rickandmorty.data.api

import com.haberturm.rickandmorty.data.entities.characters.CharactersResponseData
import com.haberturm.rickandmorty.data.entities.locations.LocationsResponseData
import retrofit2.Response
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET(AllApi.CHARACTERS)
    suspend fun getCharacters(): Response<CharactersResponseData>

    @GET(AllApi.LOCATIONS)
    suspend fun getLocations(): Response<LocationsResponseData>
}