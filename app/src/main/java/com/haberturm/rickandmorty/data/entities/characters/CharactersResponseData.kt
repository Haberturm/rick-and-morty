package com.haberturm.rickandmorty.data.entities.characters

import com.google.gson.annotations.SerializedName


data class CharactersResponseData (

    @SerializedName("info"    ) var info    : CharactersInfoData,
    @SerializedName("results" ) var results : ArrayList<CharacterResultsData>

)