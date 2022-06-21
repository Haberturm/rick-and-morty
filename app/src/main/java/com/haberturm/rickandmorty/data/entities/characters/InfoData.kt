package com.haberturm.rickandmorty.data.entities.characters

import com.google.gson.annotations.SerializedName


data class InfoData (

  @SerializedName("count" ) var count : Int,
  @SerializedName("pages" ) var pages : Int,
  @SerializedName("next"  ) var next  : String?,
  @SerializedName("prev"  ) var prev  : String?

)