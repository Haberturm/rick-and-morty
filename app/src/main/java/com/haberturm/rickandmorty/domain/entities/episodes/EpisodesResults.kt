package com.haberturm.rickandmorty.domain.entities.episodes

import com.google.gson.annotations.SerializedName


data class EpisodesResults (

  var id         : Int,
  var name       : String,
  var airDate    : String,
  var episode    : String,
  var characters : ArrayList<String>,
  var url        : String,
  var created    : String

)