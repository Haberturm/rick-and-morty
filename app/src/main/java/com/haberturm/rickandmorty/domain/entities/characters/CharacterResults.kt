package com.haberturm.rickandmorty.domain.entities.characters

data class CharacterResults (

    var id       : Int,
    var name     : String,
    var status   : String,
    var species  : String,
    var type     : String,
    var gender   : String,
    var origin   : CharacterOrigin,
    var location : CharacterLocation,
    var image    : String,
    var episode  : ArrayList<String>,
    var url      : String,
    var created  : String

)