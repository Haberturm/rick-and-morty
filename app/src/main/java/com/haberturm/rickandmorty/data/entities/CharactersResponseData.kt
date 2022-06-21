package com.haberturm.rickandmorty.data.entities

import com.google.gson.annotations.SerializedName


data class CharactersResponseData (

  @SerializedName("info"    ) var info    : Info?              = Info(),
  @SerializedName("results" ) var results : ArrayList<Results> = arrayListOf()

)