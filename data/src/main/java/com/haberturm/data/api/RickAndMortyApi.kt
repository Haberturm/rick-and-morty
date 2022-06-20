package com.haberturm.data.api

import com.haberturm.data.entities.CharactersResponseData
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET(AllApi.CHARACTERS)
    suspend fun getDataList(): CharactersResponseData
}