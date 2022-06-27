package com.haberturm.rickandmorty.data.entities.locations

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "locationsInfo")
data class LocationsInfoData (
  @PrimaryKey
  val key: Int = 1,
  @SerializedName("count" ) var count : Int,
  @SerializedName("pages" ) var pages : Int,
  @SerializedName("next"  ) var next  : String?,
  @SerializedName("prev"  ) var prev  : String?

)