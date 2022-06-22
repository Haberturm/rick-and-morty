package com.haberturm.rickandmorty.data.entities.characters

import com.google.gson.annotations.SerializedName


data class CharacterLocationData (

  @SerializedName("name" ) var name : String,
  @SerializedName("url"  ) var url  : String

)