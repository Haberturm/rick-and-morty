package com.haberturm.rickandmorty.data.entities.characters

import com.google.gson.annotations.SerializedName


data class CharacterLocationData (

  @SerializedName("name" ) var locationName : String,
  @SerializedName("url"  ) var locationUrl  : String

)