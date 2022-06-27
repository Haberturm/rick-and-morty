package com.haberturm.rickandmorty.data.entities.characters

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "character")
data class CharacterResultsData (
    @PrimaryKey
    @SerializedName("id"       ) var id       : Int,
    @SerializedName("name"     ) var name     : String,
    @SerializedName("status"   ) var status   : String,
    @SerializedName("species"  ) var species  : String,
    @SerializedName("type"     ) var type     : String,
    @SerializedName("gender"   ) var gender   : String,
    @Embedded
    @SerializedName("origin"   ) var origin   : CharacterOriginData,
    @Embedded
    @SerializedName("location" ) var location : CharacterLocationData,
    @SerializedName("image"    ) var image    : String,
    @SerializedName("episode"  ) var episode  : ArrayList<String>,
    @SerializedName("url"      ) var characterUrl      : String,
    @SerializedName("created"  ) var created  : String

)