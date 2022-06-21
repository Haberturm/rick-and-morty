package com.haberturm.rickandmorty.data.entities

import com.google.gson.annotations.SerializedName


data class Origin (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("url"  ) var url  : String? = null

)