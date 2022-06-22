package com.haberturm.rickandmorty.domain.entities.episodes

data class EpisodesInfo (

  var count : Int,
  var pages : Int,
  var next  : String?,
  var prev  : String?

)