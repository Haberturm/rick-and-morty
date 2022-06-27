package com.haberturm.rickandmorty.data.entities.episodes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "episode")
data class EpisodesResultsData (
  @PrimaryKey
  @SerializedName("id"         ) var id         : Int,
  @SerializedName("name"       ) var name       : String,
  @SerializedName("air_date"   ) var airDate    : String,
  @SerializedName("episode"    ) var episode    : String,
  @SerializedName("characters" ) var characters : ArrayList<String>,
  @SerializedName("url"        ) var url        : String,
  @SerializedName("created"    ) var created    : String

)