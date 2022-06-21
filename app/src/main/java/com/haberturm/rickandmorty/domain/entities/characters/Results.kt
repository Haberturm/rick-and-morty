package com.haberturm.rickandmorty.domain.entities.characters

data class Results (

  var id       : Int,
  var name     : String,
  var status   : String,
  var species  : String,
  var type     : String,
  var gender   : String,
  var origin   : Origin,
  var location : Location,
  var image    : String,
  var episode  : ArrayList<String>,
  var url      : String,
  var created  : String

)