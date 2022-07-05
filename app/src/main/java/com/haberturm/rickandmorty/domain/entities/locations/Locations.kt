package com.haberturm.rickandmorty.domain.entities.locations

data class Locations (

  var info    : LocationsInfo?,
  var results : ArrayList<LocationResults>

)