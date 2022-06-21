package com.haberturm.rickandmorty.data.entities.characters

import com.google.gson.annotations.SerializedName


data class CharacterResponseData (

  @SerializedName("info"    ) var info    : InfoData,
  @SerializedName("results" ) var results : ArrayList<ResultsData>

)