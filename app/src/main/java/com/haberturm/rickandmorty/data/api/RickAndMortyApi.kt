package com.haberturm.rickandmorty.data.api

import com.haberturm.rickandmorty.data.entities.CharactersResponseData
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET(AllApi.CHARACTERS)
    suspend fun getDataList(): CharactersResponseData
}