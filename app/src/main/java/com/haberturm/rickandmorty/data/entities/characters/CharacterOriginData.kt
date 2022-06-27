package com.haberturm.rickandmorty.data.entities.characters

import com.google.gson.annotations.SerializedName


data class CharacterOriginData (

  @SerializedName("name" ) var originName : String,
  @SerializedName("url"  ) var originUrl  : String

)