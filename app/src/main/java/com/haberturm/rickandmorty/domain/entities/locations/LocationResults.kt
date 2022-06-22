package com.haberturm.rickandmorty.domain.entities.locations

data class LocationResults (

  var id        : Int,
  var name      : String,
  var type      : String,
  var dimension : String,
  var residents : ArrayList<String>,
  var url       : String,
  var created   : String

)