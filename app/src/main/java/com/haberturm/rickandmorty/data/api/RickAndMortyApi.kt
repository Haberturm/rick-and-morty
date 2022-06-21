package com.haberturm.rickandmorty.data.api

import com.haberturm.rickandmorty.data.entities.characters.CharacterResponseData
import retrofit2.Response
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET(AllApi.CHARACTERS)
    suspend fun getDataList(): Response<CharacterResponseData>
}