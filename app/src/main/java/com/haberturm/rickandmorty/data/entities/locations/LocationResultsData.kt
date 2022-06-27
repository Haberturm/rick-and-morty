package com.haberturm.rickandmorty.data.entities.locations

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "locations")
data class LocationResultsData (
  @PrimaryKey
  @SerializedName("id"        ) var id        : Int,
  @SerializedName("name"      ) var name      : String,
  @SerializedName("type"      ) var type      : String,
  @SerializedName("dimension" ) var dimension : String,
  @SerializedName("residents" ) var residents : ArrayList<String>,
  @SerializedName("url"       ) var url       : String,
  @SerializedName("created"   ) var created   : String

)