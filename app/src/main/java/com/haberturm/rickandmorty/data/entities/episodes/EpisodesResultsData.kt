package com.haberturm.rickandmorty.data.entities.episodes

import com.google.gson.annotations.SerializedName


data class EpisodesResultsData (

  @SerializedName("id"         ) var id         : Int,
  @SerializedName("name"       ) var name       : String,
  @SerializedName("air_date"   ) var airDate    : String,
  @SerializedName("episode"    ) var episode    : String,
  @SerializedName("characters" ) var characters : ArrayList<String>,
  @SerializedName("url"        ) var url        : String,
  @SerializedName("created"    ) var created    : String

)