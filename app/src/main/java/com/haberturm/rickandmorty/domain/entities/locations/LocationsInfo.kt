package com.haberturm.rickandmorty.domain.entities.locations

data class LocationsInfo (

  var count : Int,
  var pages : Int,
  var next  : String?,
  var prev  : String?

)