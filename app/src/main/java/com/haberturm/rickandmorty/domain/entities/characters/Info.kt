package com.haberturm.rickandmorty.domain.entities.characters

data class Info (

  var count : Int,
  var pages : Int,
  var next  : String? = null,
  var prev  : String? = null

)